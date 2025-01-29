package com.cornellappdev.uplift.ui.viewmodels

import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootNavigationViewModel @Inject constructor(
    rootNavigationRepository : RootNavigationRepository
): UpliftViewModel<RootNavigationViewModel.RootNavigationUiState>(
    initialUiState = RootNavigationUiState()
) {
    data class RootNavigationUiState(
        val navEvent: UIEvent<UpliftRootRoute>? = null,
        val popBackStack: UIEvent<Unit>? = null
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
    }
}