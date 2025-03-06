package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : ViewModel() {

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
            val email = user?.email.orEmpty()
            val netId = email.substringBefore('@')
            if (!email.endsWith("@cornell.edu") || netId.isEmpty()) {
                //TODO: Handle error (eg. display toast)
                userInfoRepository.signOut()
                return@launch
            }
            when {
                userInfoRepository.hasUser(netId) -> rootNavigationRepository.navigate(
                    UpliftRootRoute.Home
                )

                userInfoRepository.hasFirebaseUser() -> rootNavigationRepository.navigate(
                    UpliftRootRoute.ProfileCreation
                )
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
            rootNavigationRepository.navigate(UpliftRootRoute.Home)
        }
    }
}