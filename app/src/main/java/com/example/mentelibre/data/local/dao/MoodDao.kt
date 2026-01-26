package com.example.mentelibre.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mentelibre.data.local.entity.MoodEntryEntity

@Dao
interface MoodDao {

    @Insert
    suspend fun insert(entry: MoodEntryEntity)

    @Query("SELECT * FROM mood_entries WHERE date = :date LIMIT 1")
    suspend fun getMoodByDate(date: String): MoodEntryEntity?

    @Query("SELECT * FROM mood_entries WHERE date = :date ORDER BY id ASC")
    suspend fun getByDate(date: String): List<MoodEntryEntity>

    @Query("SELECT * FROM mood_entries ORDER BY date ASC")
    suspend fun getAll(): List<MoodEntryEntity>

    @Query(
        """
        SELECT * FROM mood_entries
        WHERE date >= date('now','-7 day')
        ORDER BY date ASC
        """
    )
    suspend fun getLast7Days(): List<MoodEntryEntity>

    @Query(
        """
        SELECT * FROM mood_entries
        WHERE date >= date('now','-30 day')
        ORDER BY date ASC
        """
    )
    suspend fun getLast30Days(): List<MoodEntryEntity>
}
