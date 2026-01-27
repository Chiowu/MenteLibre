package com.example.mentelibre.data.mood

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.mentelibre.R

enum class MoodType(
    val label: String,
    val description: String,   // 👈 NUEVO (para el ?)
    @DrawableRes val icon: Int,
    val color: Color,
    val score: Float
) {

    Feliz(
        label = "Feliz",
        description = "Te sientes con mucha energía, optimismo y buen ánimo.",
        icon = R.drawable.emoji_feliz,
        color = Color(0xFFA6E7A6),
        score = 0.80f
    ),

    Tranquilo(
        label = "Tranquilo",
        description = "Te sientes en calma, sin preocupaciones importantes.",
        icon = R.drawable.emoji_tranquilo,
        color = Color(0xFFFFF1A6),
        score = 0.70f
    ),

    Sereno(
        label = "Sereno",
        description = "Te sientes estable y equilibrado emocionalmente.",
        icon = R.drawable.emoji_sereno,
        color = Color(0xFFAEDCFF),
        score = 0.60f
    ),

    Neutral(
        label = "Neutral",
        description = "No sientes emociones intensas, tu estado es neutro.",
        icon = R.drawable.emoji_neutral,
        color = Color(0xFFD9D9D9),
        score = 0.50f
    ),

    Enojado(
        label = "Enojado",
        description = "Sientes molestia, frustración o irritación.",
        icon = R.drawable.emoji_enojado,
        color = Color(0xFFFFB3B3),
        score = 0.40f
    ),

    Triste(
        label = "Triste",
        description = "Te sientes decaído o con menos ánimo de lo habitual.",
        icon = R.drawable.emoji_triste,
        color = Color(0xFFCFB8FF),
        score = 0.35f
    ),

    Deprimido(
        label = "Deprimido",
        description = "Te sientes muy desanimado o emocionalmente cansado.",
        icon = R.drawable.emoji_deprimido,
        color = Color(0xFFB3C7FF),
        score = 0.25f
    )
}
