package com.example.questlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questlog.ui.QuestViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestsListFragment : Fragment() {
    // Подключаем общую ViewModel
    private val viewModel: QuestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quests_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        // Настраиваем адаптер
        val adapter = QuestAdapter { questId ->
            viewModel.completeQuest(questId) // При клике на "Выполнено"
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Наблюдаем за базой данных. Как только добавится квест - список сам обновится!
        viewModel.activeQuests.observe(viewLifecycleOwner) { quests ->
            adapter.submitList(quests)
            tvEmpty.visibility = if (quests.isEmpty()) View.VISIBLE else View.GONE
        }

        // Кнопка создания квеста
        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_questsListFragment_to_createQuestFragment)
        }
    }
}