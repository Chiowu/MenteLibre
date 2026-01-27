package com.example.mentelibre.ui.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.mood.MoodType


@Composable
fun MoodFullScreen(
    repository: MoodRepository,
    onSaved: () -> Unit
) {
    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(repository)
    )

    var selectedMood by remember { mutableStateOf<MoodType?>(null) }

    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFC1D9),
            Color(0xFFE6C8F3),
            Color(0xFFD6E6F2)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(24.dp)
    ) {

        // 🟣 TÍTULO
        Text(
            text = "Estados de ánimo",
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF7A2E3A)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Ve tu estado de ánimo a lo largo de tu día.",
            fontSize = 14.sp,
            color = Color(0xFF7A2E3A)
        )

        Spacer(Modifier.height(16.dp))

        // 📊 GRÁFICO
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            val chartPoints = listOf(
                MoodChartPoint("Lun", 0.7f, Color(0xFFA6E7A6)),
                MoodChartPoint("Mar", 0.5f, Color.LightGray),
                MoodChartPoint("Mié", 0.3f, Color(0xFFAEDCFF)),
                MoodChartPoint("Jue", 0.8f, Color(0xFFA6E7A6)),
                MoodChartPoint("Vie", 0.5f, Color.LightGray),
                MoodChartPoint("Sáb", 0.75f, Color(0xFFA6E7A6)),
                MoodChartPoint("Dom", 0.4f, Color(0xFFFFB3B3)),
            )

            MoodLineChart(
                points = chartPoints,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        // 🧠 REGISTRO
        Text(
            text = "Registra tu estado de ánimo",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF7A2E3A)
        )

        Spacer(Modifier.height(24.dp))

        // 😄 GRID DE ÁNIMOS
        MoodType.values()
            .toList()
            .chunked(4)
            .forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { mood ->
                        MoodItem(
                            mood = mood,
                            selected = mood == selectedMood,
                            onClick = { selectedMood = mood }
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

        Spacer(Modifier.weight(1f))

        // 💜 BOTÓN
        Button(
            onClick = {
                selectedMood?.let {
                    viewModel.saveMood(it)
                    onSaved()

                }
            },
            enabled = selectedMood != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7A2E3A),
                disabledContainerColor = Color(0xFFCCB2B8)
            )
        ) {
            Text("Guardar estado", fontSize = 16.sp)
        }
    }
}

@Composable
private fun MoodItem3(
    mood: MoodType,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(80.dp)
            .background(
                if (selected) mood.color.copy(alpha = 0.25f) else Color.White,
                CircleShape
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Icon(
            painter = painterResource(id = mood.icon),
            contentDescription = mood.label,
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = mood.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
