package com.example.mentelibre.data.session

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("mente_libre_session", Context.MODE_PRIVATE)

    fun saveSession(userId: Int) {
        prefs.edit()
            .putInt("USER_ID", userId)
            .putBoolean("LOGGED_IN", true)
            .apply()
    }

    fun isLoggedIn(): Boolean =
        prefs.getBoolean("LOGGED_IN", false)

    fun getUserId(): Int =
        prefs.getInt("USER_ID", -1)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
