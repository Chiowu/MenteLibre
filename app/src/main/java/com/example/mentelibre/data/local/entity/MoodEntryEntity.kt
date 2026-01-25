package com.example.mentelibre.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,           // yyyy-MM-dd
    val mood: String,           // Feliz, Tranquilo, etc
    val score: Float
)
