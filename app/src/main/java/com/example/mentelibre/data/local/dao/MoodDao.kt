package com.example.mentelibre.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mentelibre.data.local.entity.MoodEntryEntity

@Dao
interface MoodDao {

    // ---------- INSERT ----------
    @Insert
    suspend fun insert(entry: MoodEntryEntity)

    // ---------- HOY ----------
    @Query("SELECT * FROM mood_entries WHERE date = :date LIMIT 1")
    suspend fun getMoodByDate(date: String): MoodEntryEntity?

    // ---------- HISTORIAL COMPLETO ----------
    @Query("SELECT * FROM mood_entries ORDER BY date DESC")
    suspend fun getAll(): List<MoodEntryEntity>

    // ---------- ÚLTIMOS 7 DÍAS ----------
    @Query("""
        SELECT * FROM mood_entries 
        ORDER BY date DESC 
        LIMIT 7
    """)
    suspend fun getLast7Days(): List<MoodEntryEntity>

    // ---------- ÚLTIMOS 30 DÍAS ----------
    @Query("""
        SELECT * FROM mood_entries 
        ORDER BY date DESC 
        LIMIT 30
    """)
    suspend fun getLast30Days(): List<MoodEntryEntity>
}
