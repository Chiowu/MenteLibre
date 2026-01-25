package com.example.mentelibre.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodChartSection(
    weekData: List<MoodChartPoint>,
    monthData: List<MoodChartPoint>
) {
    var selected by remember { mutableStateOf(0) }

    val data = if (selected == 0) weekData else monthData

    Column {

        // ---------- SELECTOR ----------
        TabRow(
            selectedTabIndex = selected,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF8C2F45)
        ) {
            Tab(
                selected = selected == 0,
                onClick = { selected = 0 },
                text = { Text("Semana") }
            )
            Tab(
                selected = selected == 1,
                onClick = { selected = 1 },
                text = { Text("Mes") }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- GRÁFICO ----------
        MoodLineChart(points = data)
    }
}
