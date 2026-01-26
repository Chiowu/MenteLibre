package com.example.mentelibre.ui.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import coil.compose.AsyncImage
import com.example.mentelibre.data.mood.MoodResult
import com.example.mentelibre.data.profile.ProfileDataStore

@Composable
fun HomeScreen(
    moodToday: MoodResult?,
    onGoProfile: () -> Unit
) {

    val context = LocalContext.current
    val dataStore = remember { ProfileDataStore(context) }

    // 🔑 ESCUCHA CONTINUA (esto hace que se actualice al volver)
    val profileImageFlow = dataStore.profileImageUri.collectAsState(initial = null)
    val profileImageUri = profileImageFlow.value?.let { Uri.parse(it) }

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

        // 👤 FOTO PERFIL (GRANDE + CIRCULAR REAL)
        Box(
            modifier = Modifier
                .size(160.dp) // ⬅️ MÁS GRANDE (mockup)
                .clip(CircleShape)
                .background(Color(0xFFDCEFEA))
                .border(
                    width = 3.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clickable { onGoProfile() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Foto usuario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape) // 🔥 CLAVE
                )
            } else {
                Text(
                    text = "Tocar para\nagregar foto",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🟣 PUNTAJE + ÁNIMO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            HomeMiniCard(
                title = "Puntaje",
                value = moodToday?.let { "${it.percentage}%" } ?: "--",
                modifier = Modifier.weight(1f)
            )

            HomeMiniCard(
                title = "Ánimo",
                value = moodToday?.mood?.label ?: "Sin registro",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🔵 ACTIVIDAD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF3EA)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Actividad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

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
private fun HomeMiniCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEADCF8)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = title, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
