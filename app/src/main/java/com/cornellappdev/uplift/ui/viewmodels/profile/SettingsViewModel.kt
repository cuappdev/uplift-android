package com.cornellappdev.uplift.ui.viewmodels.profile

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
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

    fun onBack() {
        rootNavigationRepository.navigateUp()
    }

    fun onAboutPressed() {
        //TODO: Add route nav after implement About screen
    }

    fun onRemindersPressed() {
        rootNavigationRepository.navigate(UpliftRootRoute.Reminders)
    }

    fun onReportPressed() {
        rootNavigationRepository.navigate(UpliftRootRoute.ReportIssue)
    }

    fun onLogOut() {
        viewModelScope.launch {
            userInfoRepository.signOut()
            rootNavigationRepository.navigate(UpliftRootRoute.Profile)
        }
    }


}
