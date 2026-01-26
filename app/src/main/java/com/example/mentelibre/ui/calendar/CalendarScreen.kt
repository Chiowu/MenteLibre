package com.example.mentelibre.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextButton(onClick = onBack) {
            Text("← Volver")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Calendario emocional",
            fontSize = 22.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Funcionalidad en desarrollo.\nAquí se visualizarán los estados de ánimo por fecha.",
            fontSize = 14.sp
        )
    }
}
