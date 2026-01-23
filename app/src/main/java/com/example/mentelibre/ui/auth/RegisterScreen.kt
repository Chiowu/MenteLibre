package com.example.mentelibre.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
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
import com.example.mentelibre.local.entity.UserEntity
import com.example.mentelibre.data.session.SessionManager
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onGoLogin: () -> Unit
) {
    // ---------- CONTEXTO ----------
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
    val inputBg = Color(0xFFF3F0FF)

    // ---------- UI ----------
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

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Apodo favorito") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Apodo favorito") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = textFieldColors()
                    )

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Teléfono (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = textFieldColors()
                    )

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        isError = showErrors && emailError != null,
                        colors = textFieldColors()
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
                        colors = textFieldColors()
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
                        colors = textFieldColors()
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
                        Text("Debes aceptar los términos", color = softError, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            hasSubmitted = true
                            if (canRegister) {
                                coroutineScope.launch {
                                    val db = AppDatabase.getDatabase(context)
                                    val session = SessionManager(context)

                                    db.userDao().deleteAll()

                                    db.userDao().insert(
                                        UserEntity(
                                            name = name,
                                            email = email.trim(),
                                            password = password
                                        )
                                    )

                                    val savedUser = db.userDao().getUser()
                                    if (savedUser != null) {
                                        session.saveSession(savedUser.id)
                                        onGoLogin()
                                    }
                                }
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
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color.White)
            }
        }
    }
}
