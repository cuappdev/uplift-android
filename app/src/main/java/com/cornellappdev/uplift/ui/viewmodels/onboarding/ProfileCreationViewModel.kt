package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileCreationUiState(
    val user: FirebaseUser? = null,
    val name: String = "",
    val imageUri: Uri? = null,
    val isGoalSkipped: Boolean = false,
    val goal: Float = 0.0f // Goal slider val is stored as float but we could change this
)

@HiltViewModel
class ProfileCreationViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : UpliftViewModel<ProfileCreationUiState>(
    userInfoRepository.getFirebaseUser().let { user ->
        ProfileCreationUiState(
            user = user,
            name = user?.displayName ?: ""
        )
    }
) {

    private fun createUser() = viewModelScope.launch {
        val state = getStateValue()
        val user = state.user
        val name = user?.displayName ?: ""
        val email = user?.email
        if (email.isNullOrBlank()) {
            Log.e("Error", "Cannot create user: missing or blank email")
            userInfoRepository.signOut()
            return@launch
        }

        val netId = email.substringBefore("@")
        val isSkipped = state.isGoalSkipped
        val goal = if (isSkipped) 0 else state.goal.toInt()
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
        rootNavigationRepository.navigateUp()
    }

    fun updateGoals(newGoal: Float) {
        applyMutation {
            copy(goal = newGoal)
        }
    }

    fun onSkip() {
        applyMutation {
            copy(isGoalSkipped = true)
        }
        createUser()
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