package com.example.mentelibre.auth

object Validators {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun email(email: String): String? =
        if (email.isBlank()) "El email es obligatorio"
        else if (!emailRegex.matches(email.trim())) "Email inválido"
        else null

    fun password(pw: String): String? {
        if (pw.isBlank()) return "La contraseña es obligatoria"
        if (pw.length < 8) return "Mínimo 8 caracteres"
        if (!pw.any { it.isUpperCase() }) return "Debe tener 1 mayúscula"
        if (!pw.any { it.isLowerCase() }) return "Debe tener 1 minúscula"
        if (!pw.any { it.isDigit() }) return "Debe tener 1 número"
        return null
    }

    fun confirmPassword(pw: String, confirm: String): String? =
        if (confirm.isBlank()) "Confirma tu contraseña"
        else if (pw != confirm) "Las contraseñas no coinciden"
        else null
}
