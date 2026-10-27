package com.example.questlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quests")
data class Quest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val xpReward: Int,
    val goldReward: Int = 0,
    val isCompleted: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis(),
    val category: String = "study"
)