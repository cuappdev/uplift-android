package com.cornellappdev.uplift.ui.viewmodels.reminders

import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MainRemindersUiState(
    val toggledItem: Boolean = false,
)

@HiltViewModel
class MainRemindersViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository
)
    : UpliftViewModel<MainRemindersUiState>(MainRemindersUiState())
{

    fun onBack() {
        rootNavigationRepository.navigateUp()
    }

    fun onClassReminderPressed() {
        rootNavigationRepository.navigate(UpliftRootRoute.ClassReminder)
    }

    fun onWorkoutRemindersPressed() {
        rootNavigationRepository.navigate(UpliftRootRoute.WorkoutReminders)
    }

    fun onCapacityRemindersPressed() {
        rootNavigationRepository.navigate(UpliftRootRoute.CapacityReminders)
    }

}

