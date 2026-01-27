package com.example.mentelibre.data.profile

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "profile_prefs")

class ProfileDataStore(private val context: Context) {

    companion object {
        private val IMAGE_URI = stringPreferencesKey("profile_image_uri")
        private val USER_NAME = stringPreferencesKey("profile_user_name")
        private val USER_EMAIL = stringPreferencesKey("profile_user_email")
        private val USER_PHONE = stringPreferencesKey("profile_user_phone")
    }

    // ---------- READ ----------
    val profileImageUri: Flow<String?> =
        context.dataStore.data.map { it[IMAGE_URI] }

    val userName: Flow<String?> =
        context.dataStore.data.map { it[USER_NAME] }

    val userEmail: Flow<String?> =
        context.dataStore.data.map { it[USER_EMAIL] }

    val userPhone: Flow<String?> =
        context.dataStore.data.map { it[USER_PHONE] }

    // ---------- WRITE ----------
    suspend fun saveProfileImage(uri: String) {
        context.dataStore.edit { it[IMAGE_URI] = uri }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL] = email }
    }

    suspend fun saveUserPhone(phone: String) {
        context.dataStore.edit { it[USER_PHONE] = phone }
    }
}