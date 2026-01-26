package com.example.mentelibre.data.mood

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.mentelibre.R

enum class MoodType(
    val label: String,
    @DrawableRes val icon: Int,
    val color: Color,
    val score: Float
) {
    Feliz(
        label = "Feliz",
        icon = R.drawable.emoji_feliz,
        color = Color(0xFFA6E7A6),
        score = 0.80f
    ),
    Tranquilo(
        label = "Tranquilo",
        icon = R.drawable.emoji_tranquilo,
        color = Color(0xFFFFF1A6),
        score = 0.70f
    ),
    Sereno(
        label = "Sereno",
        icon = R.drawable.emoji_sereno,
        color = Color(0xFFAEDCFF),
        score = 0.60f
    ),
    Neutral(
        label = "Neutral",
        icon = R.drawable.emoji_neutral,
        color = Color(0xFFD9D9D9),
        score = 0.50f
    ),
    Enojado(
        label = "Enojado",
        icon = R.drawable.emoji_enojado,
        color = Color(0xFFFFB3B3),
        score = 0.40f
    ),
    Triste(
        label = "Triste",
        icon = R.drawable.emoji_triste,
        color = Color(0xFFCFB8FF),
        score = 0.35f
    ),
    Deprimido(
        label = "Deprimido",
        icon = R.drawable.emoji_deprimido,
        color = Color(0xFFB3C7FF),
        score = 0.25f
    )
}
