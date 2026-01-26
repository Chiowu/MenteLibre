package com.example.mentelibre.data.profile

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("profile_prefs")

class ProfileDataStore(private val context: Context) {

    companion object {
        private val IMAGE_URI = stringPreferencesKey("profile_image_uri")
        private val USER_NAME = stringPreferencesKey("profile_user_name")
    }

    val profileImageUri: Flow<String?> =
        context.dataStore.data.map { it[IMAGE_URI] }

    val userName: Flow<String?> =
        context.dataStore.data.map { it[USER_NAME] }

    suspend fun saveProfileImage(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[IMAGE_URI] = uri
        }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
        }
    }
}
