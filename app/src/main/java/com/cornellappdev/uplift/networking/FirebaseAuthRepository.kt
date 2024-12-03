package com.cornellappdev.uplift.networking

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun firebaseAuthWithGoogle(idToken: String): AuthResult {
        // Create a credential using the Google ID Token
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            // Sign in with the Firebase auth using the credential
            firebaseAuth.signInWithCredential(credential).await()
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Error signing in with Google")
            throw e
        }
    }
}