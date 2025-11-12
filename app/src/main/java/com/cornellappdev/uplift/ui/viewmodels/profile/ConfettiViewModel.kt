package com.cornellappdev.uplift.ui.viewmodels.profile

import com.cornellappdev.uplift.data.repositories.ConfettiRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//Source:  Resell

/** A [ConfettiViewModel] listens for confetti events and exposes animation state to the UI. */
@HiltViewModel
class ConfettiViewModel @Inject constructor(
    confettiRepository: ConfettiRepository
) :
    UpliftViewModel<ConfettiViewModel.ConfettiUiState>(
        initialUiState = ConfettiUiState()
    ) {
    data class ConfettiUiState(
        val showing: Boolean = false
    )

    /** Sets UI state to show the confetti animation. */
    fun onShow() {
        applyMutation { copy(showing = true) }
    }

    /** Resets UI state after the animation completes to hide confetti. */
    fun onAnimationFinished() {
        applyMutation { copy(showing = false) }
    }

    init {
        asyncCollect(confettiRepository.showConfettiEvent) { event ->
            event?.consume {
                applyMutation {
                    copy(
                        showing = true
                    )
                }
            }
        }
    }
}