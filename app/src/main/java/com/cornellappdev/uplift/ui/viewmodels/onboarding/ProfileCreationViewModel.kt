package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : UpliftViewModel<ProfileCreationUiState>(
    userInfoRepository.getFirebaseUser().let { user ->
        ProfileCreationUiState(
            user = user,
            name = user?.displayName ?: "",
            imageUri = savedStateHandle.get<String>(PHOTO_URI_KEY)?.let(Uri::parse)
        )
    }
) {

    private companion object {
        const val TAG = "ProfileCreationViewModel"
        const val PHOTO_URI_KEY = "onboardingPhotoUri"
    }

    private fun createUser() = viewModelScope.launch {
        val state = getStateValue()
        val user = state.user
        val name = user?.displayName ?: ""
        val email = user?.email
        val imageUri = state.imageUri
        if (email.isNullOrBlank()) {
            Log.e(TAG, "Cannot create user: missing or blank email")
            userInfoRepository.signOut()
            return@launch
        }

        val netId = email.substringBefore("@")
        val isSkipped = state.isGoalSkipped
        val goal = if (isSkipped) 0 else state.goal.toInt()
        if (userInfoRepository.createUser(email, name, netId, isSkipped, goal, imageUri)) {
            Log.d(TAG, "User created successfully")
        } else {
            //TODO: Add error handling
            Log.e(TAG, "User not created")
            userInfoRepository.signOut()
        }
    }

    fun onPhotoSelected(uri: Uri) {
        // Remembers the selection (photo's location) so the ViewModel can restore it.
        // Also retains permissions to read that photo after the app restarts.
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: SecurityException) {
            Log.e(TAG, "Cannot retain access to selected profile photo", e)
            return
        }
        savedStateHandle[PHOTO_URI_KEY] = uri.toString()
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
