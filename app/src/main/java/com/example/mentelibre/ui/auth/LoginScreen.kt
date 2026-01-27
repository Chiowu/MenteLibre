package com.example.mentelibre.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.R
import com.example.mentelibre.auth.Validators
import com.example.mentelibre.data.local.AppDatabase
import com.example.mentelibre.data.session.SessionManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegister: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Estados de formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPw by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) } // si las credenciales no coinciden

    // Validaciones
    val emailError = Validators.email(email)
    val passwordError = Validators.password(password)

    val showEmailError = hasSubmitted && emailError != null
    val showPasswordError = hasSubmitted && passwordError != null

    val canLogin = emailError == null && passwordError == null

    // Colores
    val primaryLila = Color(0xFF7A6CF0)
    val softError = Color(0xFFC06C84)

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo gradiente
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

        // Estrellas animadas
        AnimatedStarsBackground()

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.ilustracion_bienestar),
                contentDescription = null,
                modifier = Modifier.size(280.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Tu bienestar importa",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4A3F6B)
            )

            Text(
                text = "Estamos contigo, paso a paso",
                fontSize = 22.sp,
                color = Color(0xFF4A3F6B).copy(alpha = 0.85f)
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = primaryLila,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            loginError = false
                        },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        isError = showEmailError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showEmailError) {
                        Text(emailError!!, color = softError, fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            loginError = false
                        },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        isError = showPasswordError,
                        visualTransformation =
                            if (showPw) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(onClick = { showPw = !showPw }) {
                                Text(if (showPw) "Ocultar" else "Mostrar", color = primaryLila)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showPasswordError) {
                        Text(passwordError!!, color = softError, fontSize = 13.sp)
                    }

                    if (loginError) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Correo o contraseña incorrectos",
                            color = softError,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            hasSubmitted = true
                            loginError = false // resetea error cada vez que se clickea

                            // Solo intentar login si los campos pasan validación
                            if (emailError == null && passwordError == null) {
                                coroutineScope.launch {
                                    val db = AppDatabase.getDatabase(context)
                                    val session = SessionManager(context)

                                    val user =
                                        db.userDao().getUserByEmailAndPassword(email.trim(), password)

                                    if (user != null) {
                                        session.saveSession(user.id)
                                        onLoginSuccess()
                                    } else {
                                        loginError = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryLila)
                    ) {
                        Text("Entrar")
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("¿No tienes una cuenta? ")
                        Text(
                            "Ingresa aquí",
                            color = primaryLila,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onRegister() }
                        )
                    }
                }
            }
        }
    }
}