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
        "Feliz",
        R.drawable.emoji_feliz,
        Color(0xFFA6E7A6),
        0.80f
    ),
    Tranquilo(
        "Tranquilo",
        R.drawable.emoji_tranquilo,
        Color(0xFFFFF1A6),
        0.70f
    ),
    Sereno(
        "Sereno",
        R.drawable.emoji_sereno,
        Color(0xFFAEDCFF),
        0.60f
    ),
    Neutral(
        "Neutral",
        R.drawable.emoji_neutral,
        Color(0xFFD9D9D9),
        0.50f
    ),
    Enojado(
        "Enojado",
        R.drawable.emoji_enojado,
        Color(0xFFFFB3B3),
        0.40f
    ),
    Triste(
        "Triste",
        R.drawable.emoji_triste,
        Color(0xFFCFB8FF),
        0.35f
    ),
    Deprimido(
        "Deprimido",
        R.drawable.emoji_deprimido,
        Color(0xFFB3C7FF),
        0.25f
    )
}
