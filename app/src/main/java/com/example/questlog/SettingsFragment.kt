package com.example.questlog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.questlog.ui.QuestViewModel

class SettingsFragment : Fragment() {
    private val viewModel: QuestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnReset = view.findViewById<Button>(R.id.btnReset)

        btnReset.setOnClickListener {
            viewModel.deleteAllQuests() // Чистим базу
            val prefs = requireActivity().getSharedPreferences("quest_prefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply() // Чистим сессию

            // Перезапускаем приложение
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }
    }
}