package com.example.questlog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.questlog.ui.QuestViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator

class ProfileFragment : Fragment() {
    private val viewModel: QuestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvHeroName = view.findViewById<TextView>(R.id.tvHeroName)
        val tvLevel = view.findViewById<TextView>(R.id.tvLevel)
        val tvCompletedCount = view.findViewById<TextView>(R.id.tvCompletedCount)
        val tvXpProgressText = view.findViewById<TextView>(R.id.tvXpProgressText)
        val progressBarXp = view.findViewById<LinearProgressIndicator>(R.id.progressBarXp)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val prefs = requireActivity().getSharedPreferences("quest_prefs", Context.MODE_PRIVATE)
        val heroName = prefs.getString("hero_name", "Путник")
        tvHeroName.text = heroName

        // Слушаем общий опыт и высчитываем уровни с прогрессией
        viewModel.totalXp.observe(viewLifecycleOwner) { totalXp ->
            var level = 1
            var xpRemaining = totalXp
            var xpNeeded = 100 // Для перехода на 2 уровень нужно 100

            // Вычитаем опыт уровней, пока не найдем текущий
            while (xpRemaining >= xpNeeded) {
                xpRemaining -= xpNeeded
                level++
                xpNeeded = (xpNeeded * 1.5).toInt() // Каждый следующий уровень на 50% больше
            }

            tvLevel.text = "Уровень $level"
            tvXpProgressText.text = "$xpRemaining / $xpNeeded XP"

            // Полоска прогресса
            progressBarXp.max = xpNeeded
            progressBarXp.progress = xpRemaining
        }

        viewModel.completedCount.observe(viewLifecycleOwner) { count ->
            tvCompletedCount.text = "Выполнено квестов: $count"
        }

        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            val intent = android.content.Intent(requireContext(), MainActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}