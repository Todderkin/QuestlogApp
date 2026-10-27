package com.example.questlog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.questlog.data.Quest

class QuestAdapter(private val onCompleteClick: (Long) -> Unit) :
    ListAdapter<Quest, QuestAdapter.QuestViewHolder>(DiffCallback) {

    class QuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvQuestTitle)
        val desc: TextView = itemView.findViewById(R.id.tvQuestDesc)
        val xp: TextView = itemView.findViewById(R.id.tvQuestXp)
        val btnComplete: Button = itemView.findViewById(R.id.btnComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quest, parent, false)
        return QuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val quest = getItem(position)

        // Переводим категорию с английского (из БД) на русский
        val categoryName = when (quest.category) {
            "work"   -> "Работа"
            "health" -> "Здоровье"
            "sport"  -> "Спорт"
            else     -> "Учёба"
        }

        holder.title.text = "$categoryName | ${quest.title}"

        // Если описание пустое, скрываем его
        if (quest.description.isBlank()) {
            holder.desc.visibility = View.GONE
        } else {
            holder.desc.visibility = View.VISIBLE
            holder.desc.text = quest.description
        }

        // Выводим опыт и золото
        holder.xp.text = "+${quest.xpReward} XP • +${quest.goldReward} G"

        holder.btnComplete.setOnClickListener {
            onCompleteClick(quest.id)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Quest>() {
        override fun areItemsTheSame(oldItem: Quest, newItem: Quest) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Quest, newItem: Quest) = oldItem == newItem
    }
}