package com.example.mentelibre.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.data.mood.MoodResult

@Composable
fun HistoryScreen(
    history: List<MoodResult>,
    onBack: () -> Unit
) {

    val primaryWine = Color(0xFF8C2F45)
    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFE6EC),
            Color(0xFFF6D8E8),
            Color(0xFFEADCF5)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(24.dp)
    ) {

        // ---------- HEADER ----------
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Volver", color = primaryWine)
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Historial de ánimo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = primaryWine
            )
        }

        Spacer(Modifier.height(24.dp))

        if (history.isEmpty()) {
            Text(
                text = "Aún no tienes registros",
                color = Color.Gray
            )
        } else {

            history.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(item.mood.icon),
                            contentDescription = item.mood.label,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(Modifier.width(16.dp))

                        Column {
                            Text(
                                text = item.mood.label,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "${item.percentage}% · ${item.level}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
