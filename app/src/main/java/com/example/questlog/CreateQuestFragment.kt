package com.example.questlog

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.questlog.ui.QuestViewModel
import com.google.android.material.button.MaterialButton

class CreateQuestFragment : Fragment() {
    private val viewModel: QuestViewModel by activityViewModels()

    private lateinit var btnCatStudy: MaterialButton
    private lateinit var btnCatWork: MaterialButton
    private lateinit var btnCatHealth: MaterialButton
    private lateinit var btnCatSport: MaterialButton

    private var selectedCategory = "study"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_quest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val rgXp = view.findViewById<RadioGroup>(R.id.rgXp)
        val rgGold = view.findViewById<RadioGroup>(R.id.rgGold)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnCatStudy = view.findViewById(R.id.btnCatStudy)
        btnCatWork = view.findViewById(R.id.btnCatWork)
        btnCatHealth = view.findViewById(R.id.btnCatHealth)
        btnCatSport = view.findViewById(R.id.btnCatSport)

        // Сброс формы при входе
        etTitle.text?.clear()
        rgXp.check(R.id.rbXp25)
        rgGold.check(R.id.rbGold25)
        selectedCategory = "study"
        updateCategoryButtonsUI()

        btnCatStudy.setOnClickListener { selectedCategory = "study"; updateCategoryButtonsUI() }
        btnCatWork.setOnClickListener { selectedCategory = "work"; updateCategoryButtonsUI() }
        btnCatHealth.setOnClickListener { selectedCategory = "health"; updateCategoryButtonsUI() }
        btnCatSport.setOnClickListener { selectedCategory = "sport"; updateCategoryButtonsUI() }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Введите название квеста!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val xp = when (rgXp.checkedRadioButtonId) {
                R.id.rbXp50  -> 50
                R.id.rbXp75  -> 75
                R.id.rbXp100 -> 100
                else         -> 25
            }

            val gold = when (rgGold.checkedRadioButtonId) {
                R.id.rbGold50  -> 50
                R.id.rbGold75  -> 75
                R.id.rbGold100 -> 100
                else           -> 25
            }

            viewModel.addQuest(title, "", xp, gold, selectedCategory)
            Toast.makeText(requireContext(), "Квест добавлен!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        btnCancel.setOnClickListener {
            if (etTitle.text.isNotBlank()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Отменить создание?")
                    .setMessage("Введённые данные будут потеряны.")
                    .setPositiveButton("Да, выйти") { _, _ ->
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Продолжить", null)
                    .show()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun updateCategoryButtonsUI() {
        val goldColor = ContextCompat.getColor(requireContext(), R.color.primary_gold)
        val darkBg = ContextCompat.getColor(requireContext(), R.color.surface_dark)
        val textDark = ContextCompat.getColor(requireContext(), R.color.bg_dark)
        val textWhite = ContextCompat.getColor(requireContext(), R.color.text_primary)

        btnCatStudy.backgroundTintList = ColorStateList.valueOf(if (selectedCategory == "study") goldColor else darkBg)
        btnCatStudy.setTextColor(if (selectedCategory == "study") textDark else textWhite)

        btnCatWork.backgroundTintList = ColorStateList.valueOf(if (selectedCategory == "work") goldColor else darkBg)
        btnCatWork.setTextColor(if (selectedCategory == "work") textDark else textWhite)

        btnCatHealth.backgroundTintList = ColorStateList.valueOf(if (selectedCategory == "health") goldColor else darkBg)
        btnCatHealth.setTextColor(if (selectedCategory == "health") textDark else textWhite)

        btnCatSport.backgroundTintList = ColorStateList.valueOf(if (selectedCategory == "sport") goldColor else darkBg)
        btnCatSport.setTextColor(if (selectedCategory == "sport") textDark else textWhite)
    }
}