package com.example.mentelibre.ui.diary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import java.time.LocalDateTime

@Composable
fun DiaryScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var todayScores by remember { mutableStateOf<List<Float>>(emptyList()) }

    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFE6EC),
            Color(0xFFF6D8E8),
            Color(0xFFEADCF5)
        )
    )

    // Cargar emociones del día
    LaunchedEffect(Unit) {
        todayScores = AppDatabase
            .getDatabase(context)
            .moodDao()
            .getTodayMoods()
            .map { it.score }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(24.dp)
    ) {

        // ---------- HEADER ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("← Volver")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Diario emocional",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "¿Cómo te sientes ahora?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        // ---------- EMOJIS ----------
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MoodType.values().forEach { mood ->
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = if (selectedMood == mood) mood.color else Color.White,
                            shape = CircleShape
                        )
                        .clickable {
                            selectedMood = mood

                            scope.launch {
                                val db = AppDatabase.getDatabase(context)

                                db.moodDao().insert(
                                    MoodEntryEntity(
                                        dateTime = LocalDateTime.now().toString(),
                                        mood = mood.name,
                                        score = mood.score
                                    )
                                )

                                todayScores = todayScores + mood.score
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(mood.icon),
                        contentDescription = mood.label,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}
