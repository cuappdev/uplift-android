package com.cornellappdev.uplift.ui.viewmodels.onboarding

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalsPromptUiState(
    val name: String = "",
    val imageUri: Uri? = null
)

@HiltViewModel
class GoalsPromptViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : UpliftViewModel<GoalsPromptUiState>(GoalsPromptUiState()) {

    private val _goalValue = MutableStateFlow(0f)
    val goalValue: StateFlow<Float> = _goalValue.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO: Change this later to reflect correct inputs
            val user = userInfoRepository.getFirebaseUser()
            val name = user?.displayName ?: ""
            applyMutation {
                copy(name = name)
            }
        }
    }

    fun onGoalValueChange(newValue: Float) {
        _goalValue.value = newValue
    }

    // TODO: Change skip and google sign in to reflect correct behavior
    fun onSkip() {
        navigateToHome()
    }

    fun onSignInWithGoogle() {
        navigateToHome()
    }

    fun navigateToHome() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }
}