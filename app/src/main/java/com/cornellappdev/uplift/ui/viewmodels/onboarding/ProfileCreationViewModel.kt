package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileCreationUiState(
    val name: String = "",
    val imageUri: Uri? = null
)

@HiltViewModel
class ProfileCreationViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : UpliftViewModel<ProfileCreationUiState>(ProfileCreationUiState(name = "")) {

    init {
        viewModelScope.launch {
            val user = userInfoRepository.getFirebaseUser()
            val name = user?.displayName ?: ""
            applyMutation {
                copy(name = name)
            }
        }
    }

    fun createUser() {
        viewModelScope.launch{
            val user = userInfoRepository.getFirebaseUser()
            val name = user?.displayName ?: ""
            val email = user?.email ?: ""
            val netId = email.substring(0, email.indexOf('@'))
            if (userInfoRepository.createUser(email, name, netId)) {
                navigateToHome()
            } else {
                //TODO: Add error handling
                Log.e("Error", "User not created")
                userInfoRepository.signOut()
            }
        }


    }

    fun onPhotoSelected(uri: Uri) {
        //TODO: Once backend finishes image upload endpoint, add call here
        applyMutation {
            copy(imageUri = uri)
        }
    }

    private fun navigateToHome() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }
}