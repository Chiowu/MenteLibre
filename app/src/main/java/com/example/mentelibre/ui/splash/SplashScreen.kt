package com.example.mentelibre.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.R
import kotlinx.coroutines.delay

private enum class SplashStep {
    LOGO,
    LOADING
}

@Composable
fun SplashScreen(
    onFinish: () -> Unit
) {
    var step by remember { mutableStateOf(SplashStep.LOGO) }

    // Tiempos finales
    LaunchedEffect(Unit) {
        delay(2200)              // Logo + frases
        step = SplashStep.LOADING
        delay(1800)              // Loader
        onFinish()
    }

    // Fondo pastel definitivo
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFC1CC), // rosado suave
            Color(0xFFD9B8F3), // lila
            Color(0xFFCBE7EC)  // celeste claro
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {

        when (step) {

            // LOGO + TEXTO FINAL
            SplashStep.LOGO -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_mente_libre),
                        contentDescription = "Logo Mente Libre",
                        modifier = Modifier.size(430.dp)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "Bienvenido a Mente Libre",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Un espacio seguro para conectar contigo",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // CARGANDO LIMPIO
            SplashStep.LOADING -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "Cargando...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
