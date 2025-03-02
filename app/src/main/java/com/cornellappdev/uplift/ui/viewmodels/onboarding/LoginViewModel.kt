package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isUserSignedIn: Boolean,
    val hasSkipped: Boolean,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : UpliftViewModel<LoginUiState>(LoginUiState(isUserSignedIn = false, hasSkipped = false)) {

    init {
        viewModelScope.launch {
            if (userInfoRepository.hasFirebaseUser()){
                val user = userInfoRepository.getFirebaseUser()
                val email = user?.email ?: ""
                val netId = email.substring(0, email.indexOf('@'))
                val userExists = userInfoRepository.hasUser(netId)
                applyMutation {
                    copy(isUserSignedIn = userExists)
                }
            }
            val hasSkipped = userInfoRepository.getSkipFromDataStore()
            applyMutation {
                copy(hasSkipped = hasSkipped)
            }
        }
    }

    fun onSignInWithGoogle(credential: Credential) {
        if (credential !is CustomCredential || credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            //TODO: Handle error
            Log.e("Error", "Unexpected credential")
            return
        }
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val idToken = googleIdTokenCredential.idToken
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e("Error", throwable.message.orEmpty(), throwable)
        }) {
            userInfoRepository.signInWithGoogle(idToken)
            val user = userInfoRepository.getFirebaseUser()
            val email = user?.email ?: ""
            val netId = email.substring(0, email.indexOf('@'))
            when {
                userInfoRepository.hasUser(netId) -> navigateToProfileCreation()
                userInfoRepository.hasFirebaseUser() -> {
                    if (!email.endsWith("@cornell.edu")){
                        //TODO: Handle error (eg. display toast)
                        userInfoRepository.signOut()
                    } else {
                        navigateToProfileCreation()
                    }
                }
                //TODO: Handle error
                else -> {
                    Log.e("Error", "Unexpected credential")
                    userInfoRepository.signOut()
                }
            }
        }
    }

    fun onSkip() {
        viewModelScope.launch {
            userInfoRepository.storeSkip(true)
            applyMutation {
                copy(hasSkipped = true)
            }
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }

    private fun navigateToProfileCreation() {
        rootNavigationRepository.navigate(UpliftRootRoute.ProfileCreation)
    }
}