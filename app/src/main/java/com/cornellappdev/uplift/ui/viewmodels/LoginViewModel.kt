package com.cornellappdev.uplift.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.FireStoreRepository
import com.cornellappdev.uplift.data.repositories.FirebaseAuthRepository
import com.cornellappdev.uplift.data.repositories.GoogleAuthRepository
import com.cornellappdev.uplift.data.repositories.UpliftAuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository,
    private val fireStoreRepository: FireStoreRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val upliftAuthRepository: UpliftAuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    fun navigateIfLoggedIn() {
        if(googleAuthRepository.accountOrNull() != null) {
            onSignInCompleted(
                id = googleAuthRepository.accountOrNull()!!.id!!,
                email = googleAuthRepository.accountOrNull()!!.email!!,
            )
        }
    }

    private fun onSignInFailed() {
        googleAuthRepository.signOut();
    }

    private fun onSignInCompleted(id: String, email: String) {
        if(!email.endsWith("@cornell.edu")) {
            googleAuthRepository.signOut()
            return
        }

        viewModelScope.launch {
            try {
                firebaseAuthRepository.firebaseAuthWithGoogle(id);

                fireStoreRepository.getUserOnboarded(
                    email = email,
                    onError = { onSignInFailed()},
                    onSuccess = { onboarded ->

                    }
                    )

                val netid = email.substring(0, email.indexOf('@'))
                val name = googleAuthRepository.accountOrNull()!!.displayName!!

                val response = upliftAuthRepository.createUser(email, name, netid)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error getting user: ", e);
                onSignInFailed()
            }
        }
    }

    @Composable
    fun makeSignInLauncher(): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return googleAuthRepository.googleLoginLauncher(
            onError = ::onSignInFailed,
            onGoogleSignInCompleted = ::onSignInCompleted
        )
    }

    fun getSignInClient(): GoogleSignInClient {
        val gso = googleAuthRepository.googleSignInClient
        val client = GoogleSignIn.getClient(context, gso)

        return client
    }
}