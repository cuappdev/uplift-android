package com.cornellappdev.uplift.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val fileName = "encrypted_tokens"

    // Initialize EncryptedSharedPreferences
    private val sharedPreferences: SharedPreferences? by lazy {
        try {
            createEncryptedPrefs()
        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to initialize EncryptedSharedPreferences", e)
            // Clear corrupted state
            context.deleteSharedPreferences(fileName)
            try {
                // Could have failed due to previous corrupted state
                // One more attempt after cleaning the corruption
                createEncryptedPrefs()
            } catch (retryException: Exception) {
                // Probably broken return null
                Log.e("TokenManager", "Failed to initialize EncryptedSharedPreferences again", retryException)
                null
            }
        }
    }

    private fun createEncryptedPrefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences?.edit {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
        }
    }

    fun getAccessToken(): String? = sharedPreferences?.getString("access_token", null)

    fun getRefreshToken(): String? = sharedPreferences?.getString("refresh_token", null)

    fun clearTokens() {
        sharedPreferences?.edit { clear() }
    }

    fun saveUserSession(userId: Int, username: String, userEmail: String) {
        sharedPreferences?.edit {
            putInt("user_id", userId)
            putString("username", username)
            putString("user_email", userEmail)
        }
    }

    fun getUserId(): Int? = sharedPreferences?.takeIf { it.contains("user_id") }?.getInt("user_id", -1)

}