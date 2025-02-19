package com.cornellappdev.uplift.data.repositories

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseMessagingRepository @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) {
    suspend fun getDeviceFCMToken(): String? {
        return try {
            // Get the FCM token
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            // Handle any exceptions that occur
            e.printStackTrace()
            null
        }
    }
}