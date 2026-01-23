package com.example.mentelibre.data.session

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("mente_libre_session", Context.MODE_PRIVATE)

    fun saveSession(userId: Int) {
        prefs.edit()
            .putBoolean("logged", true)
            .putInt("userId", userId)
            .apply()
    }

    fun isLogged(): Boolean =
        prefs.getBoolean("logged", false)

    fun getUserId(): Int =
        prefs.getInt("userId", -1)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
