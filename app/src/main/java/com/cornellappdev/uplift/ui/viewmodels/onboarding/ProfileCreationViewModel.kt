package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileCreationUiState(
    val user: FirebaseUser? = null,
    val name: String = "",
    val imageUri: Uri? = null,
    val isGoalSkipped: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val goal: Int = 1,
    val isCreating: Boolean = false,
    val retryInPlace: Boolean = false,
    val canRetry: Boolean = true
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

    private var creationJob: Job? = null

    private fun createUser() {
        if (creationJob?.isActive == true) return
        creationJob = viewModelScope.launch {
            applyMutation { copy(errorMessage = null, retryInPlace = false, canRetry = true) }
            val state = getStateValue()
            val user = state.user
            val email = user?.email
            if (email.isNullOrBlank()) {
                Log.e(TAG, "Cannot create user: missing or blank email")
                applyMutation { copy(errorMessage = R.string.profile_sign_in_error_message, retryInPlace = false, canRetry = true) }
                return@launch
            }

            applyMutation { copy(isCreating = true) }
            try {
                val success = userInfoRepository.createUser(
                    email = email,
                    name = user?.displayName.orEmpty(),
                    netId = email.substringBefore("@"),
                    skip = state.isGoalSkipped,
                    goal = if (state.isGoalSkipped) 0 else state.goal,
                    imageUri = state.imageUri
                )
                if (success) {
                    Log.d(TAG, "User created successfully")
                } else {
                    Log.e(TAG, "User not created")
                    applyMutation { copy(errorMessage = R.string.profile_create_error_message, retryInPlace = true, canRetry = true) }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Account creation failed", e)
                applyMutation { copy(errorMessage = R.string.profile_create_error_message, retryInPlace = true, canRetry = true) }
            } finally {
                applyMutation { copy(isCreating = false) }
            }
        }
    }

    fun onErrorRetry() {
        if (getStateValue().retryInPlace) {
            createUser()
        } else {
            retryOnboarding()
        }
    }

    private fun retryOnboarding() {
        if (creationJob?.isActive == true) return
        creationJob = viewModelScope.launch {
            savedStateHandle.remove<String>(PHOTO_URI_KEY)
            applyMutation { ProfileCreationUiState() }
            try {
                userInfoRepository.signOut()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Sign out failed during onboarding retry", e)
            }
            rootNavigationRepository.navigate(UpliftRootRoute.Onboarding)
        }
    }

    fun dismissError() {
        applyMutation { copy(errorMessage = null, retryInPlace = false, canRetry = true) }
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
            applyMutation { copy(errorMessage = R.string.profile_photo_read_error_message, retryInPlace = false, canRetry = false) }
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

    fun updateGoals(newGoal: Int) {
        if (getStateValue().isCreating) return
        applyMutation {
            copy(goal = newGoal)
        }
    }

    fun onSkip() {
        if (creationJob?.isActive == true) return
        applyMutation {
            copy(isGoalSkipped = true)
        }
        createUser()
    }

    fun onNext() {
        if (creationJob?.isActive == true) return
        applyMutation { copy(isGoalSkipped = false) }
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