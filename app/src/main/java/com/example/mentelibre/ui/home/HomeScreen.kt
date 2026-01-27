package com.example.mentelibre.ui.home

import android.net.Uri
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.data.local.AppDatabase
import coil.compose.AsyncImage
import com.example.mentelibre.data.mood.MoodResult
import com.example.mentelibre.data.profile.ProfileDataStore

@Composable
fun HomeScreen(
    moodToday: MoodResult?,
    onGoProfile: () -> Unit,
    onRefresh: () -> Unit,
    onGoMood: () -> Unit
) {

    LaunchedEffect(Unit) { onRefresh() }

    val context = LocalContext.current
    val dataStore = remember { ProfileDataStore(context) }

    // Nombre de usuario desde DB
    val userName by produceState<String?>(initialValue = null) {
        val db = AppDatabase.getDatabase(context)
        value = db.userDao().getUser()?.name
    }

    // Foto de perfil desde DataStore (reactiva)
    val profileImageUri by dataStore.profileImageUri.collectAsState(initial = null)
    val profileUri = profileImageUri?.let { Uri.parse(it) }

    // Fuerza recarga de Coil si la Uri es la misma
    val forceReload = remember(profileUri) { System.currentTimeMillis() }
    val greeting = remember {
        listOf(
            "Hola",
            "Qué bueno verte",
            "Me alegra verte de nuevo",
            "Bienvenido de vuelta"
        ).random()
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
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 👋 SALUDO
        Text(
            text = "$greeting${userName?.let { ", $it" } ?: ""} !",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4A3F6B),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // 👤 FOTO PERFIL
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCEFEA))
                .border(3.dp, Color.White, CircleShape)
                .clickable { onGoProfile() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = "Tocar para\nagregar foto",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(36.dp))

        // ⭐ PUNTAJE + ÁNIMO (SEPARADOS)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            HomeStatusCard(
                title = "Puntaje",
                value = moodToday?.percentage,
                placeholder = "--",
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clickable { onGoMood() }
            )

            HomeStatusCard(
                title = "Ánimo",
                textValue = moodToday?.mood?.label,
                placeholder = "Sin registro",
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clickable { onGoMood() }
            )
        }

        Spacer(Modifier.height(12.dp))

        // 💜 MENSAJE HUMANO
        Text(
            text = if (moodToday == null)
                "Cuéntanos cómo te sientes hoy 💜"
            else
                "Gracias por registrar cómo te sientes ✨",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF7A2E43),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onGoMood() },
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(36.dp))

        // 📊 ACTIVIDAD
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF3EA)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Actividad", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeStatusCard(
    title: String,
    value: Int? = null,
    textValue: String? = null,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateIntAsState(
        targetValue = value ?: 0,
        label = "scoreAnim"
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEADCF8)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(title, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(Modifier.height(8.dp))

            Text(
                text = when {
                    value != null -> "$animatedValue%"
                    textValue != null -> textValue
                    else -> placeholder
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3F6B),
                textAlign = TextAlign.Center
            )
        }
    }
}
