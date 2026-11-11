package com.cornellappdev.uplift.ui.viewmodels.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.auth.SessionManager
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isLoggedIn: Boolean = false,
    val showDeleteError: Boolean = false,
    val isDeleting: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository,
    sessionManager: SessionManager,
) : UpliftViewModel<SettingsUiState>(
    SettingsUiState(isLoggedIn = sessionManager.isLoggedIn.value)
) {
    init {
        asyncCollect(sessionManager.isLoggedIn) { isLoggedIn ->
            applyMutation { copy(isLoggedIn = isLoggedIn) }
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }

    fun dismissError() {
        applyMutation { copy(showDeleteError = false) }
    }

    fun onBack() {
        if (getStateValue().isDeleting) return
        rootNavigationRepository.navigateUp()
    }

    fun onAboutPressed() {
        if (getStateValue().isDeleting) return
        rootNavigationRepository.navigate(UpliftRootRoute.About)
    }

    fun onReportPressed() {
        if (getStateValue().isDeleting) return
        rootNavigationRepository.navigate(UpliftRootRoute.ReportIssue)
    }

    fun onLogOut() {
        if (getStateValue().isDeleting) return
        viewModelScope.launch {
            try {
                userInfoRepository.signOut()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Sign out failed during logout", e)
            }
            rootNavigationRepository.navigate(UpliftRootRoute.Onboarding)
        }
    }

    private var deleteAccountJob: Job? = null

    fun onDeleteAccount() {
        if (deleteAccountJob?.isActive == true) return
        deleteAccountJob = viewModelScope.launch {
            applyMutation { copy(showDeleteError = false, isDeleting = true) }
            try {
                val success = userInfoRepository.deleteAccount()
                if (success) {
                    rootNavigationRepository.navigate(UpliftRootRoute.Onboarding)
                } else {
                    applyMutation { copy(showDeleteError = true) }
                    Log.e(TAG, "Account could not be deleted")
                }
            } finally {
                applyMutation { copy(isDeleting = false) }
            }
        }
    }
}
