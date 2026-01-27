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
fun MoodRegisterSection(
    repository: MoodRepository,
    onBack: () -> Unit
) {

    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(repository)
    )

    // 🔹 estados del ViewModel
    val chartData by viewModel.chartData.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()

    // 🔹 estados locales
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var showHelp by remember { mutableStateOf(false) }

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

        // 🔙 BACK + TITLE
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Estados de ánimo",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Registra y observa cómo evoluciona tu ánimo.",
            color = Color.DarkGray
        )

        Spacer(Modifier.height(20.dp))

        // 🟣 TABS FUNCIONALES
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(30.dp))
                .padding(4.dp)
        ) {
            listOf(
                MoodPeriod.DAYS to "Días",
                MoodPeriod.WEEKS to "Semanas",
                MoodPeriod.MONTHS to "Meses"
            ).forEach { (period, label) ->
                val selected = selectedPeriod == period

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (selected) Color(0xFF7A2E43) else Color.Transparent,
                            RoundedCornerShape(24.dp)
                        )
                        .clickable { viewModel.changePeriod(period) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (selected) Color.White else Color.DarkGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 📊 CHART
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            if (chartData.isNotEmpty()) {
                MoodLineChart(points = chartData)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 📝 TITLE + HELP
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Registra tu estado de ánimo",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(onClick = { showHelp = true }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_help),
                    contentDescription = "Ayuda"
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 😀 EMOJIS + NOMBRE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MoodType.values().take(5).forEach { mood ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(64.dp)
                        .clickable { selectedMood = mood }
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (mood == selectedMood) mood.color else Color.White,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = mood.icon),
                            contentDescription = mood.label,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = mood.label,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // 💾 GUARDAR (permite cambiar varias veces)
        Button(
            onClick = {
                selectedMood?.let {
                    viewModel.saveMood(it)
                    selectedMood = null
                }
            },
            enabled = selectedMood != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A2E43))
        ) {
            Text("Guardar estado", fontSize = 16.sp, color = Color.White)
        }
    }

    // ❓ DIALOGO DE AYUDA
    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            title = { Text("¿Qué significa cada estado?") },
            text = {
                Column {
                    MoodType.values().take(5).forEach {
                        Text("• ${it.label}: ${it.description}")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) {
                    Text("Entendido")
                }
            }
        )
    }
}
