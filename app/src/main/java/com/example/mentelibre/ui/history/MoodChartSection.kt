package com.example.mentelibre.ui.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodChartSection(
    weekPercentage: Int,
    monthPercentage: Int
) {
    var selectedPeriod by remember { mutableStateOf("week") }

    val percentage =
        if (selectedPeriod == "week") weekPercentage else monthPercentage

    Column {

        // ---------- SELECTOR ----------
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = selectedPeriod == "week",
                onClick = { selectedPeriod = "week" },
                label = { Text("Semana") }
            )

            FilterChip(
                selected = selectedPeriod == "month",
                onClick = { selectedPeriod = "month" },
                label = { Text("Mes") }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- GRÁFICO ----------
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val barHeight = size.height * (percentage / 100f)

            drawRect(
                color = Color(0xFF8C2F45),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width / 4,
                    height = barHeight
                ),
                topLeft = androidx.compose.ui.geometry.Offset(
                    x = size.width / 2 - (size.width / 8),
                    y = size.height - barHeight
                )
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Promedio ${if (selectedPeriod == "week") "semanal" else "mensual"}: $percentage%",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
