package com.cornellappdev.uplift.ui.viewmodels.reminders

import com.cornellappdev.uplift.data.repositories.CapacityRemindersRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CapacityRemindersUiState(
    val toggledOn: Boolean = false,
    val selectedDays: Set<String> = emptySet(),
    val capacityThreshold: Float = 0.5f,
    val selectedGyms: Set<String> = emptySet(),
)
@HiltViewModel
class CapacityRemindersViewModel @Inject constructor(
    private val capacityRemindersRepository: CapacityRemindersRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<CapacityRemindersUiState>(CapacityRemindersUiState()) {
    init{

    }
    fun setToggle(toggledOn: Boolean) {
        applyMutation { copy(toggledOn = toggledOn) }
    }

    fun setSelectedDays(days: Set<String>) {
        applyMutation { copy(selectedDays = days) }
    }

    fun setCapacityThreshold(threshold: Float) {
        applyMutation { copy(capacityThreshold = threshold) }
    }

    fun setSelectedGyms(gyms: Set<String>) {
        applyMutation { copy(selectedGyms = gyms) }
    }

    fun onBack(){
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }
}

