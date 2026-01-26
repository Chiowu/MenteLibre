package com.example.mentelibre.ui.mood

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.data.local.AppDatabase
import com.example.mentelibre.data.local.entity.MoodEntryEntity
import com.example.mentelibre.data.mood.MoodType
import kotlinx.coroutines.launch

import java.time.LocalDate

@Composable
fun MoodRegisterScreen(
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var error by remember { mutableStateOf(false) }

    // datos reales del gráfico (desde Room)
    var todayScores by remember { mutableStateOf<List<Float>>(emptyList()) }

    // ---------- CARGAR DATOS DEL DÍA ----------
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val today = LocalDate.now().toString()
        todayScores = db.moodDao()
            .getByDate(today)
            .map { it.score }
    }

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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Estados de ánimo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        // ---------- GRÁFICO ----------
        MoodLineChart(
            scores = todayScores,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Registra tu estado de ánimo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        // ---------- EMOJIS ----------
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MoodType.values().forEach { mood ->
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = if (selectedMood == mood)
                                mood.color
                            else
                                Color.White.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                        .clickable {
                            selectedMood = mood
                            error = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(mood.icon),
                        contentDescription = mood.label,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (error) {
            Text(
                text = "Debes seleccionar un estado de ánimo",
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- BOTÓN GUARDAR ----------
        Button(
            onClick = {
                if (selectedMood == null) {
                    error = true
                    return@Button
                }

                scope.launch {
                    val db = AppDatabase.getDatabase(context)
                    val today = LocalDate.now().toString()

                    // guardar en Room
                    db.moodDao().insert(
                        MoodEntryEntity(
                            date = today,
                            mood = selectedMood!!.name,
                            score = selectedMood!!.score
                        )
                    )

                    // volver a leer los datos del día
                    todayScores = db.moodDao()
                        .getByDate(today)
                        .map { it.score }

                    onSaved()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Guardar estado", fontSize = 16.sp)
        }
    }
}
