package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.util.Log
import androidx.annotation.StringRes
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : ViewModel() {

    // Message for the "Can't sign in" ProfileErrorDialog, or null when it's hidden
    private val _signInErrorMessage = MutableStateFlow<Int?>(null)
    val signInErrorMessage: StateFlow<Int?> = _signInErrorMessage.asStateFlow()

    fun dismissSignInError() {
        _signInErrorMessage.value = null
    }

    private fun showSignInError(@StringRes message: Int) {
        _signInErrorMessage.value = message
    }

    fun onSignInWithGoogle(credential: Credential) {
        if (credential !is CustomCredential || credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            Log.e("LoginViewModel", "Unexpected credential type: ${credential.type}")
            showSignInError(R.string.sign_in_generic_error_message)
            return
        }
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val idToken = googleIdTokenCredential.idToken
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            // e.g. Google/Firebase sign-in failing offline
            Log.e("LoginViewModel", "Sign-in failed", throwable)
            showSignInError(R.string.sign_in_error_message)
            viewModelScope.launch {
                try {
                    userInfoRepository.signOut()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.e("LoginViewModel", "Sign out after failed sign-in failed", e)
                }
            }
        }) {
            userInfoRepository.signInWithGoogle(idToken)
            val user = userInfoRepository.getFirebaseUser()
            val email = user?.email.orEmpty()
            val netId = email.substringBefore('@')
            if (!email.endsWith("@cornell.edu") || netId.isEmpty()) {
                Log.e("LoginViewModel", "Signed in with a non-Cornell account")
                userInfoRepository.signOut()
                showSignInError(R.string.sign_in_cornell_error_message)
                return@launch
            }
            val hasUser = userInfoRepository.hasUser(netId).getOrElse { error ->
                // Don't send an existing user to onboarding just because the check failed
                Log.e("LoginViewModel", "Couldn't check for an existing account", error)
                userInfoRepository.signOut()
                showSignInError(R.string.sign_in_error_message)
                return@launch
            }
            when {
                hasUser -> {
                    val success = userInfoRepository.loginUser(netId)
                    if (success) {
                        Log.d("LoginViewModel", "User logged in successfully")
                    } else {
                        userInfoRepository.signOut()
                        showSignInError(R.string.sign_in_error_message)
                    }
                }
                userInfoRepository.hasFirebaseUser() -> {
                    rootNavigationRepository.navigate(UpliftRootRoute.ProfileCreation)
                }
                else -> {
                    Log.e("LoginViewModel", "No Firebase user after Google sign-in")
                    userInfoRepository.signOut()
                    showSignInError(R.string.sign_in_generic_error_message)
                }
            }
        }
    }

    fun onSkip() {
        viewModelScope.launch {
            userInfoRepository.storeSkip(true)
            rootNavigationRepository.navigate(UpliftRootRoute.Home)
        }
    }
}