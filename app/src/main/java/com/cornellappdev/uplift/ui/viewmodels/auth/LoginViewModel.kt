package com.cornellappdev.uplift.ui.viewmodels.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.FirebaseAuthRepository
import com.cornellappdev.uplift.data.repositories.GoogleAuthRepository
import com.cornellappdev.uplift.data.repositories.UpliftAuthRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val upliftAuthRepository: UpliftAuthRepository,
    private val rootNavigationRepository: RootNavigationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    fun navigateIfLoggedIn() {
        if (googleAuthRepository.accountOrNull() != null) {
            onSignInCompleted(
                id = googleAuthRepository.accountOrNull()!!.id!!,
                email = googleAuthRepository.accountOrNull()!!.email!!,
            )
        }
    }

    private fun onSignOut() {
        googleAuthRepository.signOut();
    }

    private fun onSignInCompleted(id: String, email: String) {
        if (!email.endsWith("@cornell.edu")) {
            googleAuthRepository.signOut()
            return
        }

        viewModelScope.launch {
            try {
                firebaseAuthRepository.firebaseAuthWithGoogle(id);

                val netid = email.substring(0, email.indexOf('@'))
                val name = googleAuthRepository.accountOrNull()!!.displayName!!

                val response = upliftAuthRepository.createUser(email, name, netid)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error getting user: ", e);
                onSignOut()
            }
        }
    }

    fun onSignUpWithGoogle(credential: Credential) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.e("Error", throwable.message.orEmpty(), throwable)
            },
            block = {
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
//                    accountService.signInWithGoogle(googleIdTokenCredential.idToken)
//                    openAndPopUp(NOTES_LIST_SCREEN, SIGN_UP_SCREEN)
                } else {
                    Log.e("Error", "Unexpected credential")
                }
            }
        )
    }

    @Composable
    fun makeSignInLauncher(): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return googleAuthRepository.googleLoginLauncher(
            onError = ::onSignOut,
            onGoogleSignInCompleted = ::onSignInCompleted
        )
    }

    fun getSignInClient(): GoogleSignInClient {
        val gso = googleAuthRepository.googleSignInClient
        val client = GoogleSignIn.getClient(context, gso)

        return client
    }

    fun navigateToHome() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }

    fun navigateToProfileCreation() {
        rootNavigationRepository.navigate(UpliftRootRoute.ProfileCreation)
    }
}