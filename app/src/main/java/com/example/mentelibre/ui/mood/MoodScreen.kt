package com.example.mentelibre.ui.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mentelibre.data.mood.MoodRepository

@Composable
fun MoodScreen(
    repository: MoodRepository,
    onBack: () -> Unit
) {
    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(repository)
    )

    // 🔹 NUEVO flujo correcto
    val chartData by viewModel.chartData.collectAsState()

    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFE6EC),
            Color(0xFFF3DAE6),
            Color(0xFFE9E1F2)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {

        // 🔙 HEADER
        Text(
            text = "Estados de ánimo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7A2E43)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Ve tu estado de ánimo a lo largo del tiempo.",
            fontSize = 14.sp,
            color = Color(0xFF7A2E43)
        )

        Spacer(Modifier.height(16.dp))

        // 📊 GRÁFICO
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            if (chartData.isNotEmpty()) {
                MoodLineChart(points = chartData)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 📝 REGISTRO DE ÁNIMO
        MoodRegisterSection(
            repository = repository,
            onBack = onBack
        )
    }
}
