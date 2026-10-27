package com.example.questlog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests WHERE isCompleted = 0 ORDER BY dateCreated DESC")
    fun getActiveQuests(): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE isCompleted = 1 ORDER BY dateCreated DESC")
    fun getCompletedQuests(): Flow<List<Quest>>

    @Insert
    suspend fun insertQuest(quest: Quest)

    @Query("UPDATE quests SET isCompleted = 1 WHERE id = :questId")
    suspend fun completeQuest(questId: Long)

    @Query("DELETE FROM quests WHERE id = :questId")
    suspend fun deleteQuest(questId: Long)

    @Query("SELECT COALESCE(SUM(xpReward), 0) FROM quests WHERE isCompleted = 1")
    fun getTotalXp(): Flow<Int>

    @Query("SELECT COALESCE(SUM(goldReward), 0) FROM quests WHERE isCompleted = 1")
    fun getTotalGold(): Flow<Int>

    @Query("SELECT COUNT(*) FROM quests WHERE isCompleted = 1")
    fun getCompletedQuestsCount(): Flow<Int>

    @Query("DELETE FROM quests")
    suspend fun deleteAllQuests()
}