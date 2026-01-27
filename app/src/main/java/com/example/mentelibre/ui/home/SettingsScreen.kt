package com.example.mentelibre.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onProfile: () -> Unit,
    onSecurity: () -> Unit,
    onSupport: () -> Unit,
    onSuggestions: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLogout: () -> Unit
) {
    val bg = Brush.verticalGradient(
        listOf(Color(0xFFFFE6EC), Color(0xFFF6D8E8), Color(0xFFEADCF5))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", fontSize = 29.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4A3F6B)) },
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
            // ITEMS DE AJUSTES
            SettingsItem(
                text = "Perfil",
                icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF4A3F6B)) },
                onClick = onProfile
            )
            Spacer(Modifier.height(12.dp))

            SettingsItem(
                text = "Seguridad",
                icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF4A3F6B)) },
                onClick = onSecurity
            )
            Spacer(Modifier.height(12.dp))

            SettingsItem(
                text = "Soporte",
                icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF4A3F6B)) },
                onClick = onSupport
            )
            Spacer(Modifier.height(12.dp))

            SettingsItem(
                text = "Sugerencias",
                icon = { Icon(Icons.Default.Feedback, contentDescription = null, tint = Color(0xFF4A3F6B)) },
                onClick = onSuggestions
            )
            Spacer(Modifier.height(12.dp))

            SettingsItem(
                text = "Eliminar cuenta",
                icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                onClick = onDeleteAccount,
                textColor = Color.Red
            )
            Spacer(Modifier.height(12.dp))

            SettingsItem(
                text = "Cerrar sesión",
                icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red) },
                onClick = onLogout,
                textColor = Color.Red
            )
        }
    }
}

@Composable
fun SettingsItem(
    text: String,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    textColor: Color = Color(0xFF4A3F6B)
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEADCF8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(Modifier.width(12.dp))
            }
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
    }
}