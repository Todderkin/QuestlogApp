package com.example.questlog

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.questlog.ui.QuestViewModel

class DashboardFragment : Fragment() {
    private val viewModel: QuestViewModel by activityViewModels()

    private var currentTotalGold = 0
    private lateinit var prefs: android.content.SharedPreferences

    private lateinit var tvGold: TextView
    private lateinit var layoutShop: LinearLayout
    private lateinit var layoutInventory: LinearLayout
    private lateinit var btnTabShop: Button
    private lateinit var btnTabInventory: Button

    // Кнопки использования предметов
    private lateinit var btnUseCoffee: Button
    private lateinit var btnUseGaming: Button
    private lateinit var btnUseDayOff: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().getSharedPreferences("quest_prefs", Context.MODE_PRIVATE)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        tvGold = view.findViewById(R.id.tvGold)

        layoutShop = view.findViewById(R.id.layoutShop)
        layoutInventory = view.findViewById(R.id.layoutInventory)
        btnTabShop = view.findViewById(R.id.btnTabShop)
        btnTabInventory = view.findViewById(R.id.btnTabInventory)

        btnUseCoffee = view.findViewById(R.id.btnUseCoffee)
        btnUseGaming = view.findViewById(R.id.btnUseGaming)
        btnUseDayOff = view.findViewById(R.id.btnUseDayOff)

        val btnToArchive = view.findViewById<Button>(R.id.btnToArchive)
        btnToArchive.setOnClickListener {
            findNavController().navigate(R.id.archiveFragment)
        }

        val heroName = prefs.getString("hero_name", "Путник")
        tvGreeting.text = "Привет, $heroName!"

        viewModel.totalGold.observe(viewLifecycleOwner) { goldEarned ->
            currentTotalGold = goldEarned
            updateGoldUI()
        }

        btnTabShop.setOnClickListener { showShop() }
        btnTabInventory.setOnClickListener { showInventory() }

        setupShopButtons(view)
        setupInventoryButtons(view)
        updateInventoryUI()
    }

    private fun updateGoldUI() {
        val spentGold = prefs.getInt("spent_gold", 0)
        val availableGold = currentTotalGold - spentGold
        tvGold.text = "$availableGold G"
    }

    private fun buyItem(itemKey: String, cost: Int) {
        val spentGold = prefs.getInt("spent_gold", 0)
        val availableGold = currentTotalGold - spentGold

        if (availableGold >= cost) {
            prefs.edit().putInt("spent_gold", spentGold + cost).apply()
            val currentCount = prefs.getInt(itemKey, 0)
            prefs.edit().putInt(itemKey, currentCount + 1).apply()

            updateGoldUI()
            updateInventoryUI() // Обновляем инвентарь (и цвета кнопок)
            Toast.makeText(requireContext(), "Успешно куплено!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Недостаточно золота!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupShopButtons(view: View) {
        view.findViewById<Button>(R.id.btnBuyCoffee).setOnClickListener { buyItem("item_coffee", 20) }
        view.findViewById<Button>(R.id.btnBuyGaming).setOnClickListener { buyItem("item_gaming", 25) }
        view.findViewById<Button>(R.id.btnBuyDayOff).setOnClickListener { buyItem("item_dayoff", 100) }
    }

    private fun updateInventoryUI() {
        val coffee = prefs.getInt("item_coffee", 0)
        val gaming = prefs.getInt("item_gaming", 0)
        val dayOff = prefs.getInt("item_dayoff", 0)

        view?.findViewById<TextView>(R.id.tvInvCoffee)?.text = "Чашка кофе (в наличии: $coffee)"
        view?.findViewById<TextView>(R.id.tvInvGaming)?.text = "Час игры (в наличии: $gaming)"
        view?.findViewById<TextView>(R.id.tvInvDayOff)?.text = "Выходной от квестов (в наличии: $dayOff)"

        // Перекрашиваем кнопки в зависимости от наличия
        updateButtonState(btnUseCoffee, coffee > 0)
        updateButtonState(btnUseGaming, gaming > 0)
        updateButtonState(btnUseDayOff, dayOff > 0)
    }

    // Метод для изменения цвета кнопок (желтый если есть, серый если нет)
    private fun updateButtonState(button: Button, isAvailable: Boolean) {
        val bgColor = if (isAvailable) R.color.primary_gold else R.color.surface_dark
        val textColor = if (isAvailable) R.color.bg_dark else R.color.text_secondary

        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), bgColor))
        button.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    private fun useItem(itemKey: String, itemName: String, actionText: String) {
        val count = prefs.getInt(itemKey, 0)
        if (count > 0) {
            prefs.edit().putInt(itemKey, count - 1).apply()
            updateInventoryUI() // Обновляем UI после использования
            Toast.makeText(requireContext(), "$itemName: $actionText", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "У вас нет $itemName!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupInventoryButtons(view: View) {
        btnUseCoffee.setOnClickListener { useItem("item_coffee", "Кофе", "Вы чувствуете прилив бодрости!") }
        btnUseGaming.setOnClickListener { useItem("item_gaming", "Час игры", "Время отдохнуть за играми!") }
        btnUseDayOff.setOnClickListener { useItem("item_dayoff", "Выходной", "Квесты подождут, сегодня отдыхаем!") }
    }

    private fun showShop() {
        layoutShop.visibility = View.VISIBLE
        layoutInventory.visibility = View.GONE

        btnTabShop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_gold))
        btnTabShop.setTextColor(ContextCompat.getColor(requireContext(), R.color.bg_dark))

        btnTabInventory.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface_dark))
        btnTabInventory.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
    }

    private fun showInventory() {
        layoutShop.visibility = View.GONE
        layoutInventory.visibility = View.VISIBLE

        btnTabInventory.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_gold))
        btnTabInventory.setTextColor(ContextCompat.getColor(requireContext(), R.color.bg_dark))

        btnTabShop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface_dark))
        btnTabShop.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
    }
}