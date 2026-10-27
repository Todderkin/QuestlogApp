package com.example.questlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questlog.ui.QuestViewModel

class ArchiveFragment : Fragment() {
    private val viewModel: QuestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewArchive)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyArchive)

        val adapter = QuestAdapter { _ ->
            // В архиве кнопка "Выполнено" ничего не делает
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.completedQuests.observe(viewLifecycleOwner) { quests ->
            adapter.submitList(quests)
            tvEmpty.visibility = if (quests.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}