package com.example.mentelibre.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.auth.Validators

@Composable
fun RegisterScreen(
    onRegister: (name: String, email: String, password: String) -> Unit,
    onGoLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }

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

    // 🎨 Colores pastel
    val primaryLila = Color(0xFF7A6CF0)
    val softError = Color(0xFFC06C84)
    val inputBg = Color(0xFFF3F0FF)

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
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Text(
                text = "Un espacio seguro para ti",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                Column(modifier = Modifier.padding(24.dp)) {

                    fun fieldColors() = TextFieldDefaults.colors(
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        errorContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Apodo favorito") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors()
                    )

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Teléfono (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors()
                    )

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        isError = showErrors && emailError != null,
                        colors = fieldColors()
                    )

                    if (showErrors && emailError != null) {
                        Text(emailError, color = softError, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        isError = showErrors && passwordError != null,
                        colors = fieldColors()
                    )

                    if (showErrors && passwordError != null) {
                        Text(passwordError, color = softError, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        isError = showErrors && confirmError != null,
                        colors = fieldColors()
                    )

                    if (showErrors && confirmError != null) {
                        Text(confirmError, color = softError, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = acceptedTerms,
                            onCheckedChange = { acceptedTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = primaryLila)
                        )
                        Text("Acepto los términos y condiciones")
                    }

                    if (showErrors && termsError) {
                        Text(
                            "Debes aceptar los términos",
                            color = softError,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            hasSubmitted = true
                            if (canRegister) {
                                onRegister(name, email.trim(), password)
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
                        Text("Comenzar", fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onGoLogin) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión",
                    color = Color.White
                )
            }
        }
    }
}
