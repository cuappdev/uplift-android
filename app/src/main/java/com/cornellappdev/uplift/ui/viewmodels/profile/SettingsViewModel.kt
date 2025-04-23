package com.cornellappdev.uplift.ui.viewmodels.profile

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isLoggedIn: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<SettingsUiState>(SettingsUiState()) {
    init {
        viewModelScope.launch {
            if (userInfoRepository.hasFirebaseUser()) {
                val user = userInfoRepository.getFirebaseUser()
                val email = user?.email
                val netId = email?.substringBefore('@')
                val hasUser = netId?.let { userInfoRepository.hasUser(it) } == true
                applyMutation {
                    copy(isLoggedIn = hasUser)
                }
            } else {
                applyMutation {
                    copy(isLoggedIn = false)
                }
            }
        }
    }

    fun onBack(){
        rootNavigationRepository.navigate(UpliftRootRoute.Profile)
    }

    fun navigateToAbout() {
        //TODO: Add route nav after implement About screen
    }

    fun navigateToReminders() {
        rootNavigationRepository.navigate(UpliftRootRoute.Reminders)
    }

    fun navigateToReport() {
        rootNavigationRepository.navigate(UpliftRootRoute.ReportIssue)
    }

    fun onLogOut() {
        viewModelScope.launch {
            userInfoRepository.signOut()
            rootNavigationRepository.navigate(UpliftRootRoute.Profile)
        }
    }


}