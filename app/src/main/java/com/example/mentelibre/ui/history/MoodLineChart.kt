package com.example.mentelibre.ui.mood

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Gráfico de línea simple para mostrar evolución del ánimo
 * scores: valores entre 0f y 1f
 */
@Composable
fun MoodLineChart(
    scores: List<Float>,
    modifier: Modifier = Modifier
) {
    if (scores.isEmpty()) return

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val maxScore = 1f
        val minScore = 0f

        val stepX = size.width / max(1, scores.size - 1)

        val path = Path()

        scores.forEachIndexed { index, score ->
            val x = stepX * index
            val y = size.height - ((score - minScore) / (maxScore - minScore)) * size.height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // Punto
            drawCircle(
                color = Color(0xFF8C2F45),
                radius = 6f,
                center = Offset(x, y)
            )
        }

        // Línea
        drawPath(
            path = path,
            color = Color(0xFF8C2F45),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )
    }
}
