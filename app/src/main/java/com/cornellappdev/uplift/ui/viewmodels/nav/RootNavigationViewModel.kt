package com.cornellappdev.uplift.ui.viewmodels.nav

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.auth.SessionManager
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.ONBOARDING_FLAG
import com.cornellappdev.uplift.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootNavigationViewModel @Inject constructor(
    rootNavigationRepository: RootNavigationRepository,
    private val userInfoRepository: UserInfoRepository,
    val sessionManager: SessionManager
) : UpliftViewModel<RootNavigationViewModel.RootNavigationUiState>(
    initialUiState = RootNavigationUiState(
        isLoggedIn = sessionManager.isLoggedIn.value,
        startDestination = if (!ONBOARDING_FLAG) {
            UpliftRootRoute.Home
        } else if (sessionManager.isLoggedIn.value) {
            UpliftRootRoute.Home
        }
        else {
            UpliftRootRoute.Onboarding
        }
    )
) {
    data class RootNavigationUiState(
        val isLoggedIn: Boolean = false,
        val navEvent: UIEvent<UpliftRootRoute>? = null,
        val popBackStack: UIEvent<Unit>? = null,
        val navigateUp: UIEvent<Unit>? = null,
        val startDestination: UpliftRootRoute = if (ONBOARDING_FLAG) UpliftRootRoute.Onboarding else UpliftRootRoute.Home
    ) {
        internal fun withSession(loggedIn: Boolean, destination: UpliftRootRoute): RootNavigationUiState {
            val shouldNavigate = destination != startDestination || loggedIn != isLoggedIn
            return copy(
                isLoggedIn = loggedIn,
                startDestination = destination,
                navEvent = if (shouldNavigate) UIEvent(destination) else navEvent
            )
        }
    }

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

        asyncCollect(rootNavigationRepository.navigateUpFlow) { up ->
            applyMutation {
                copy(navigateUp = up)
            }
        }

        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { loggedIn ->
                val hasSkipped = userInfoRepository.getSkipFromDataStore()
                val shouldShowHome = loggedIn || hasSkipped || !ONBOARDING_FLAG
                val newRoute = if (shouldShowHome) UpliftRootRoute.Home else UpliftRootRoute.Onboarding

                applyMutation {
                    // Compare against the previous session before updating it. Guest login
                    // must finish onboarding even when Home is already the start destination.
                    withSession(loggedIn, newRoute)
                }
            }
        }
    }
}
