package com.cornellappdev.uplift.data.repositories

import com.cornellappdev.uplift.ui.viewmodels.profile.ConfettiViewModel
import com.cornellappdev.uplift.util.UIEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

//Source:  Resell

@Singleton
class ConfettiRepository @Inject constructor() {
    private val _showConfettiEvent: MutableStateFlow<UIEvent<ConfettiViewModel.ConfettiUiState>?> =
        MutableStateFlow(null)
    val showConfettiEvent = _showConfettiEvent.asStateFlow()

    fun showConfetti (event: ConfettiViewModel.ConfettiUiState) {
        _showConfettiEvent.value = UIEvent(event)
    }
}
