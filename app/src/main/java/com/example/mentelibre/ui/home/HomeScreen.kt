package com.example.mentelibre.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun HomeScreen(
    onGoRegisterMood: () -> Unit,
    onGoDiary: () -> Unit,
    onGoHistory: () -> Unit,
    moodToday: MoodResult?
) {

    val primaryWine = Color(0xFF8C2F45)
    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFE6EC),
            Color(0xFFF6D8E8),
            Color(0xFFEADCF5)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Inicio",
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                color = primaryWine
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Tu estado de ánimo hoy",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = primaryWine
                    )

                    Spacer(Modifier.height(16.dp))

                    if (moodToday == null) {

                        Text(
                            "Aún no has registrado cómo te sientes",
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = onGoRegisterMood,
                            colors = ButtonDefaults.buttonColors(containerColor = primaryWine),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Registrar ánimo")
                        }

                    } else {

                        Image(
                            painter = painterResource(moodToday.mood.icon),
                            contentDescription = moodToday.mood.label,
                            modifier = Modifier.size(120.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            moodToday.mood.label,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = primaryWine
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "${moodToday.percentage}%",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryWine
                        )

                        Text(
                            moodToday.level,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            HomeActionButton("📓 Diario", onGoDiary)
            Spacer(Modifier.height(12.dp))
            HomeActionButton("📊 Historial", onGoHistory)
        }
    }
}

@Composable
private fun HomeActionButton(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier.padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
