package com.example.mentelibre.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    onBack: () -> Unit,
    onChangePassword: () -> Unit
) {
    val bg = Brush.verticalGradient(
        listOf(Color(0xFFFFE6EC), Color(0xFFF6D8E8), Color(0xFFEADCF5))
    )

    var fingerprintEnabled by remember { mutableStateOf(false) }
    var pinEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguridad", fontSize = 29.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4A3F6B)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color(0xFF4A3F6B))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ACTIVAR HUELLA
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEADCF8)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Color(0xFF4A3F6B))
                        Spacer(Modifier.width(12.dp))
                        Text("Activar huella", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A3F6B))
                    }
                    Switch(
                        checked = fingerprintEnabled,
                        onCheckedChange = { fingerprintEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF8C2F45),
                            uncheckedThumbColor = Color.LightGray,
                            checkedTrackColor = Color(0xFFF6D8E8)
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ACTIVAR PIN
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEADCF8)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Pin, contentDescription = null, tint = Color(0xFF4A3F6B))
                        Spacer(Modifier.width(12.dp))
                        Text("Activar PIN", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A3F6B))
                    }
                    Switch(
                        checked = pinEnabled,
                        onCheckedChange = { pinEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF8C2F45),
                            uncheckedThumbColor = Color.LightGray,
                            checkedTrackColor = Color(0xFFF6D8E8)
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // CAMBIAR CONTRASEÑA
            SettingsItem(
                text = "Cambiar contraseña",
                icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF4A3F6B)) },
                onClick = onChangePassword
            )
        }
    }
}