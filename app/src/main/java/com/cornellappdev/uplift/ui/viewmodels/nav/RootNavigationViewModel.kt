package com.cornellappdev.uplift.ui.viewmodels.nav

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootNavigationViewModel @Inject constructor(
    rootNavigationRepository: RootNavigationRepository,
    private val userInfoRepository: UserInfoRepository
) : UpliftViewModel<RootNavigationViewModel.RootNavigationUiState>(
    initialUiState = RootNavigationUiState()
) {
    data class RootNavigationUiState(
        val navEvent: UIEvent<UpliftRootRoute>? = null,
        val popBackStack: UIEvent<Unit>? = null,
        val startDestination: UpliftRootRoute = UpliftRootRoute.Home
    )

    init {

        asyncCollect(rootNavigationRepository.routeFlow) { route ->
            applyMutation {
                copy(navEvent = route)
            }
        }

        asyncCollect(rootNavigationRepository.popBackStackFlow) { pop ->
            applyMutation {
                copy(popBackStack = pop)
            }
        }

        viewModelScope.launch {
            val hasSkipped = userInfoRepository.getSkipFromDataStore()
            var hasUser = false
            if (userInfoRepository.hasFirebaseUser()) {
                val user = userInfoRepository.getFirebaseUser()
                val email = user?.email
                val netId = email?.substringBefore('@')
                hasUser = netId?.let { userInfoRepository.hasUser(it) } ?: false
            }
            applyMutation {
                copy(
                    startDestination = if (hasSkipped || hasUser) UpliftRootRoute.Home else UpliftRootRoute.Onboarding
                )
            }
        }
    }
}