package com.example.mentelibre.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: String,   // Ej: 2026-01-26T14:35:00
    val mood: String,
    val score: Float
)
