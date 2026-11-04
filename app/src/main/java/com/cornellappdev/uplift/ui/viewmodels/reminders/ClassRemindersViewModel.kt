package com.cornellappdev.uplift.ui.viewmodels.reminders

import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class ReminderTimeOption {
    THIRTY_MIN,
    SIXTY_MIN,
    CUSTOM
}

data class ClassRemindersUiState(
    val toggledOn: Boolean = false,
    val timeOption: ReminderTimeOption = ReminderTimeOption.THIRTY_MIN,
    val customHours: String = "",
    val customMinutes: String = "",
    val isMinutesError: Boolean = false,
)

@HiltViewModel
class ClassRemindersViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<ClassRemindersUiState>(ClassRemindersUiState()) {

    fun onBack() = rootNavigationRepository.navigateUp()

    fun setToggle(toggledOn: Boolean) {
        applyMutation { copy(toggledOn = toggledOn) }
    }

    fun selectTimeOption(option: ReminderTimeOption) {
        applyMutation { copy(timeOption = option) }
    }

    fun onHoursChanged(input: String) {
        if (input.all { it.isDigit() } && (input.length <= 2)) {
            applyMutation { copy(customHours = input) }
        }
    }

    fun onMinutesChanged(input: String) {
        if (input.all { it.isDigit() } && (input.length <= 2)) {
            val minutesInt = input.toIntOrNull()
            val isError = (minutesInt != null) && (minutesInt > 59)
            applyMutation {
                copy(
                    customMinutes = input,
                    isMinutesError = isError
                )
            }
        }
    }
}