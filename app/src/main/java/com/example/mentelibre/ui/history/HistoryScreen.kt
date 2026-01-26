package com.example.mentelibre.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mentelibre.data.export.MoodExportUtil
import com.example.mentelibre.data.local.AppDatabase
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.mood.MoodResult
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    history: List<MoodResult>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var weekAverage by remember { mutableStateOf(0) }
    var monthAverage by remember { mutableStateOf(0) }
    var exportMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val repo = MoodRepository(db.moodDao())

        weekAverage = repo.getWeekAverage()
        monthAverage = repo.getMonthAverage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Historial emocional",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        MoodChartSection(
            weekPercentage = weekAverage,
            monthPercentage = monthAverage
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    val file = MoodExportUtil.exportToCsv(context)
                    exportMessage = "Archivo guardado: ${file.name}"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Descargar historial")
        }

        exportMessage?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
