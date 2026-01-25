package com.example.mentelibre.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mentelibre.data.local.entity.DiaryEntryEntity

@Dao
interface DiaryDao {

    // Guardar entrada del diario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntryEntity)

    // Obtener entrada por fecha (1 por día)
    @Query("SELECT * FROM diary_entries WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): DiaryEntryEntity?

    // Historial completo (para más adelante)
    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    suspend fun getAll(): List<DiaryEntryEntity>
}
