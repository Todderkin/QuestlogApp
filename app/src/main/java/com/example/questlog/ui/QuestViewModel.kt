package com.example.questlog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.questlog.data.AppDatabase
import com.example.questlog.data.Quest
import kotlinx.coroutines.launch

class QuestViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).questDao()

    val activeQuests = dao.getActiveQuests().asLiveData()
    val completedQuests = dao.getCompletedQuests().asLiveData()
    val totalXp = dao.getTotalXp().asLiveData()
    val totalGold = dao.getTotalGold().asLiveData() // НОВОЕ
    val completedCount = dao.getCompletedQuestsCount().asLiveData()

    fun addQuest(title: String, description: String, xp: Int, gold: Int, category: String = "study") {
        viewModelScope.launch {
            dao.insertQuest(
                Quest(
                    title = title,
                    description = description,
                    xpReward = xp,
                    goldReward = gold,
                    category = category
                )
            )
        }
    }

    fun completeQuest(questId: Long) {
        viewModelScope.launch { dao.completeQuest(questId) }
    }

    fun deleteQuest(questId: Long) {
        viewModelScope.launch { dao.deleteQuest(questId) }
    }

    fun deleteAllQuests() {
        viewModelScope.launch { dao.deleteAllQuests() }
    }
}