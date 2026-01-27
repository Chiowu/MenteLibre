package com.example.mentelibre.ui.mood

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun MoodLineChart(
    points: List<MoodChartPoint>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
) {
    if (points.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {

        val spacing = size.width / (points.size + 1)
        val maxHeight = size.height * 0.75f
        val baseY = size.height * 0.8f

        val path = Path()
        val offsets = mutableListOf<Offset>()

        points.forEachIndexed { index, point ->
            val x = spacing * (index + 1)
            val y = baseY - (point.value * maxHeight)
            val offset = Offset(x, y)
            offsets.add(offset)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // línea curva
        drawPath(
            path = path,
            color = Color(0xFF6B4E4E),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )

        // puntos
        offsets.forEachIndexed { index, offset ->
            drawCircle(
                color = points[index].color,
                radius = 8.dp.toPx(),
                center = offset
            )

            // etiquetas (Lun, Mar, etc)
            drawText(
                textMeasurer = textMeasurer,
                text = points[index].label,
                topLeft = Offset(offset.x - 14.dp.toPx(), baseY + 8.dp.toPx())
            )
        }
    }
}
