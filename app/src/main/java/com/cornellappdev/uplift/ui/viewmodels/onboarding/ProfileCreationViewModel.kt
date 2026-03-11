package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileCreationUiState(
    val name: String = "",
    val imageUri: Uri? = null,
    val isGoalSkipped: Boolean = false,
    val goal: Float = 0.0f // Goal slider val is stored as float but we could change this
)

@HiltViewModel
class ProfileCreationViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : UpliftViewModel<ProfileCreationUiState>(ProfileCreationUiState()) {

    init {
        viewModelScope.launch {
            val user = userInfoRepository.getFirebaseUser()
            val name = user?.displayName ?: ""
            applyMutation {
                copy(name = name)
            }
        }
    }

    private fun createUser() = viewModelScope.launch {
        val user = userInfoRepository.getFirebaseUser()
        val name = user?.displayName ?: ""
        val email = user?.email ?: ""
        val netId = email.substring(0, email.indexOf('@'))
        val isSkipped = getStateValue().isGoalSkipped
        var goal = 0
        if (!isSkipped) {
            goal = getStateValue().goal.toInt()
        }
        if (userInfoRepository.createUser(email, name, netId, isSkipped, goal)) {
            navigateToHome()
        } else {
            //TODO: Add error handling
            Log.e("Error", "User not created")
            userInfoRepository.signOut()
        }
    }

    fun onPhotoSelected(uri: Uri) {
        //TODO: Once backend finishes image upload endpoint, add call here
        applyMutation {
            copy(imageUri = uri)
        }
    }

    fun onBackClick() {
        navigateToProfileCreation()
    }

    fun updateGoals(newGoal: Int) {
        applyMutation {
            copy(goal = newGoal.toFloat())
        }
    }

    fun onSkip() {
        applyMutation {
            copy(isGoalSkipped = true)
        }
        createUser()
    }
    }

    fun onNext() {
        createUser()
    }

    private fun navigateToProfileCreation() {
        rootNavigationRepository.navigate(UpliftRootRoute.ProfileCreation)
    }


    fun navigateToGoals() {
        rootNavigationRepository.navigate(UpliftRootRoute.GoalsOnboarding)
    }

    private fun navigateToHome() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }
}