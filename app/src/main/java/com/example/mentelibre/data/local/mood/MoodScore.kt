package com.example.mentelibre.data.mood

/**
 * Utilidades para transformar puntajes de ánimo
 * en porcentajes y etiquetas visuales.
 *
 * NO depende de UI (Compose).
 * Cumple desacoplamiento de lógica.
 */
object MoodScore {

    /**
     * Convierte score (0.0 – 1.0) a porcentaje (0 – 100)
     */
    fun toPercentage(score: Float): Int {
        return (score * 100).toInt()
    }

    /**
     * Clasificación textual según score
     */
    fun level(score: Float): String {
        return when {
            score >= 0.75f -> "Muy positivo"
            score >= 0.60f -> "Positivo"
            score >= 0.45f -> "Neutral"
            score >= 0.30f -> "Bajo"
            else -> "Muy bajo"
        }
    }

    /**
     * Score promedio (para semanas / meses)
     */
    fun average(scores: List<Float>): Float {
        if (scores.isEmpty()) return 0f
        return scores.sum() / scores.size
    }
}
