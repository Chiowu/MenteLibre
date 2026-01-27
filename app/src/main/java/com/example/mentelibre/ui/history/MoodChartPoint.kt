package com.example.mentelibre.ui.mood

import androidx.compose.ui.graphics.Color

data class MoodChartPoint(
    val label: String,   // Lun, Mar, Mié...
    val value: Float,    // 0f - 1f
    val color: Color     // Positivo / Neutral / Negativo
)
