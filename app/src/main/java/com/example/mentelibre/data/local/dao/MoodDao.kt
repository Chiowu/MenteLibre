package com.example.mentelibre.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mentelibre.data.local.entity.MoodEntryEntity

@Dao
interface MoodDao {

    // ---------------- INSERTAR ----------------
    @Insert
    suspend fun insert(entry: MoodEntryEntity)

    // ---------------- HOY ----------------
    // Último estado de ánimo del día (el más reciente)
    @Query(
        """
        SELECT * FROM mood_entries
        WHERE date(dateTime) = date('now')
        ORDER BY dateTime DESC
        LIMIT 1
        """
    )
    suspend fun getTodayLastMood(): MoodEntryEntity?

    // Todos los estados de hoy (por si se necesitan)
    @Query(
        """
        SELECT * FROM mood_entries
        WHERE date(dateTime) = date('now')
        ORDER BY dateTime ASC
        """
    )
    suspend fun getTodayMoods(): List<MoodEntryEntity>

    // ---------------- HISTORIAL COMPLETO ----------------
    @Query(
        """
        SELECT * FROM mood_entries
        ORDER BY dateTime DESC
        """
    )
    suspend fun getAll(): List<MoodEntryEntity>

    // ---------------- ÚLTIMOS 7 DÍAS ----------------
    @Query(
        """
        SELECT * FROM mood_entries
        WHERE date(dateTime) >= date('now','-6 day')
        ORDER BY dateTime ASC
        """
    )
    suspend fun getLast7Days(): List<MoodEntryEntity>

    // ---------------- ÚLTIMOS 30 DÍAS ----------------
    @Query(
        """
        SELECT * FROM mood_entries
        WHERE date(dateTime) >= date('now','-29 day')
        ORDER BY dateTime ASC
        """
    )
    suspend fun getLast30Days(): List<MoodEntryEntity>
}
