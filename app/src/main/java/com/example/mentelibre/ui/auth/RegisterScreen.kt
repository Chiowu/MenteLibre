package com.example.mentelibre.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.auth.Validators
import com.example.mentelibre.data.local.AppDatabase
import com.example.mentelibre.data.profile.ProfileDataStore
import com.example.mentelibre.data.session.SessionManager
import com.example.mentelibre.local.entity.UserEntity
import kotlinx.coroutines.launch

// ---------------------------------------------
// Clase para las estrellas
data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val alpha: Float
)

// ---------------------------------------------
// Composable para las estrellas animadas
@Composable
fun AnimatedStarsBackground(modifier: Modifier = Modifier) {
    val starCount = 100 // más estrellas
    val stars = remember {
        List(starCount) {
            Star(
                x = (0..1000).random() / 1000f,       // horizontal aleatoria
                y = (0..1000).random() / 1000f,       // vertical aleatoria
                size = (2..8).random().toFloat(),     // tamaño más grande
                alpha = (3..10).random() / 10f        // opacidad variada
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlphas = stars.map { star ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f * star.alpha,
            targetValue = 1f * star.alpha,
            animationSpec = infiniteRepeatable(
                animation = tween((1500..3500).random(), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        stars.forEachIndexed { index, star ->
            drawCircle(
                color = Color.White.copy(alpha = animatedAlphas[index].value),
                radius = star.size,
                center = androidx.compose.ui.geometry.Offset(
                    star.x * w,
                    star.y * h
                )
            )
        }
    }
}

// ---------------------------------------------
// Pantalla de registro
@Composable
fun RegisterScreen(
    onGoLogin: () -> Unit,
    onGoHome: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // ---------- ESTADO ----------
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }

    // ---------- VALIDACIONES ----------
    val emailError = Validators.email(email)
    val passwordError = Validators.password(password)
    val confirmError = Validators.confirmPassword(password, confirmPassword)
    val termsError = !acceptedTerms

    val showErrors = hasSubmitted
    val canRegister =
        emailError == null &&
                passwordError == null &&
                confirmError == null &&
                acceptedTerms

    // ---------- COLORES ----------
    val primaryLila = Color(0xFF7A6CF0)
    val softError = Color(0xFFC06C84)

    Box(modifier = Modifier.fillMaxSize()) {
        // Gradiente de fondo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFC1D9),
                            Color(0xFFE6C8F3),
                            Color(0xFFD6E6F2)
                        )
                    )
                )
        )

        // Estrellas animadas encima del gradiente
        AnimatedStarsBackground()

        // Contenido de la UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear cuenta", fontSize = 28.sp, color = Color(0xFF4A3F6B))
            Text("Un espacio seguro para ti", color = Color(0xFF4A3F6B).copy(0.85f))

            Spacer(Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(24.dp)) {

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Apodo favorito") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Teléfono (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Correo electrónico") },
                        isError = showErrors && emailError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    if (showErrors && emailError != null)
                        Text(emailError, color = softError, fontSize = 12.sp)

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = showErrors && passwordError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    if (showErrors && passwordError != null)
                        Text(passwordError, color = softError, fontSize = 12.sp)

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = showErrors && confirmError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    if (showErrors && confirmError != null)
                        Text(confirmError, color = softError, fontSize = 12.sp)

                    Spacer(Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = acceptedTerms,
                            onCheckedChange = { acceptedTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = primaryLila)
                        )
                        Text("Acepto los términos y condiciones")
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            hasSubmitted = true
                            if (canRegister) {
                                coroutineScope.launch {
                                    val db = AppDatabase.getDatabase(context)
                                    val session = SessionManager(context)
                                    val profileDataStore = ProfileDataStore(context)

                                    db.userDao().deleteAll()
                                    db.userDao().insert(
                                        UserEntity(
                                            name = name,
                                            email = email.trim(),
                                            password = password
                                        )
                                    )

                                    db.userDao().getUser()?.let {
                                        session.saveSession(it.id)
                                        onGoHome()
                                    }

                                    profileDataStore.saveUserName(name)
                                    profileDataStore.saveUserEmail(email.trim())
                                    profileDataStore.saveUserPhone(phone)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryLila)
                    ) {
                        Text("Comenzar")
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("¿Ya tienes una cuenta? ")
                        Text(
                            "Ingresa aquí",
                            color = primaryLila,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onGoLogin() }
                        )
                    }
                }
            }
        }
    }
}