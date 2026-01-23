package com.example.mentelibre.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.R
import com.example.mentelibre.auth.Validators

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit
) {
    // ---------- ESTADO ----------
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPw by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }

    // ---------- VALIDACIONES ----------
    val emailError = Validators.email(email)
    val passwordError = if (password.isBlank()) "La contraseña es obligatoria" else null

    val showEmailError = hasSubmitted && emailError != null
    val showPasswordError = hasSubmitted && passwordError != null

    val canLogin = emailError == null && passwordError == null

    // ---------- COLORES ----------
    val primaryLila = Color(0xFF7A6CF0)
    val softError = Color(0xFFC06C84)

    // ---------- UI ----------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.ilustracion_bienestar),
            contentDescription = null,
            modifier = Modifier.size(280.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Tu bienestar importa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Text(
            text = "Estamos contigo, paso a paso",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.85f)
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
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    isError = showEmailError,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryLila,
                        unfocusedBorderColor = Color.LightGray,
                        errorBorderColor = softError
                    )
                )

                if (showEmailError) {
                    Text(
                        text = emailError!!,
                        color = softError,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    isError = showPasswordError,
                    visualTransformation = if (showPw)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPw = !showPw }) {
                            Text(
                                if (showPw) "Ocultar" else "Mostrar",
                                color = primaryLila
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryLila,
                        unfocusedBorderColor = Color.LightGray,
                        errorBorderColor = softError
                    )
                )

                if (showPasswordError) {
                    Text(
                        text = passwordError!!,
                        color = softError,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        hasSubmitted = true
                        if (canLogin) {
                            onLogin(email.trim(), password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryLila,
                        disabledContainerColor = primaryLila.copy(alpha = 0.4f)
                    )
                ) {
                    Text("Entrar", fontSize = 16.sp)
                }

                Spacer(Modifier.height(16.dp))

                TextButton(
                    onClick = onRegister,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "Crear cuenta",
                        color = primaryLila,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
