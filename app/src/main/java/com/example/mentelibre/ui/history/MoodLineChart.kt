package com.example.mentelibre.ui.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun MoodLineChart(
    points: List<MoodChartPoint>
) {
    if (points.isEmpty()) return

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val maxValue = 100f
        val spacing = size.width / (points.size - 1)

        val path = Path()
        val strokeWidth = 6f

        points.forEachIndexed { index, point ->
            val x = spacing * index
            val y = size.height - (point.value / maxValue) * size.height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // puntos
            drawCircle(
                color = Color(0xFF8C2F45),
                radius = 8f,
                center = Offset(x, y)
            )
        }

        drawPath(
            path = path,
            color = Color(0xFF8C2F45),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}
