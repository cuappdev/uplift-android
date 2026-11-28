package com.cornellappdev.uplift.ui.viewmodels.reminders

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.ClassRemindersRepository
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ReminderTimeOption {
    THIRTY_MIN, SIXTY_MIN, CUSTOM
}

data class ClassRemindersUiState(
    val toggledOn: Boolean = false,
    val timeOption: ReminderTimeOption = ReminderTimeOption.THIRTY_MIN,
    val customHours: String = "",
    val customMinutes: String = "",
    val isMinutesError: Boolean = false,
    val showInvalidTimeDialog: Boolean = false
)

@HiltViewModel
class ClassRemindersViewModel @Inject constructor(
    private val classRemindersRepository: ClassRemindersRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<ClassRemindersUiState>(ClassRemindersUiState()) {

    init {
        viewModelScope.launch {
            val toggledOn = classRemindersRepository.getToggle()
            val timeOptionStr = classRemindersRepository.getTimeOption()
            val hours = classRemindersRepository.getHours()
            val minutes = classRemindersRepository.getMinutes()

            val option = try {
                if (timeOptionStr != null) ReminderTimeOption.valueOf(timeOptionStr) else ReminderTimeOption.THIRTY_MIN
            } catch (_: Exception) {
                ReminderTimeOption.THIRTY_MIN
            }

            val minutesInt = minutes.toIntOrNull()
            val isError = (minutesInt != null) && (minutesInt > 59)

            applyMutation {
                copy(
                    toggledOn = toggledOn,
                    timeOption = option,
                    customHours = hours,
                    customMinutes = minutes,
                    isMinutesError = isError
                )
            }
        }
    }

    fun onBack() {
        val currentState = uiStateFlow.value
        val hours = currentState.customHours.toIntOrNull() ?: 0
        val minutes = currentState.customMinutes.toIntOrNull() ?: 0

        val invalidCustomTime = currentState.toggledOn &&
                currentState.timeOption == ReminderTimeOption.CUSTOM &&
                (currentState.isMinutesError || (hours == 0 && minutes == 0))

        if (invalidCustomTime) {
            applyMutation { copy(showInvalidTimeDialog = true) }
            return // do not go back to reminder screens
        }

        viewModelScope.launch {
            if (currentState.toggledOn) {
                classRemindersRepository.scheduleClassReminder()
            }
            rootNavigationRepository.navigateUp()
        }
    }

    fun setToggle(toggledOn: Boolean) {
        val hasMinutesError = uiStateFlow.value.isMinutesError
        applyMutation { copy(toggledOn = toggledOn) }
        viewModelScope.launch {
            classRemindersRepository.setToggle(toggledOn)
            if (toggledOn && !hasMinutesError) {
                classRemindersRepository.scheduleClassReminder()
            } else {
                classRemindersRepository.cancelClassReminder()
            }
        }
    }

    fun selectTimeOption(option: ReminderTimeOption) {
        applyMutation { copy(timeOption = option) }
        viewModelScope.launch {
            classRemindersRepository.setTimeOption(option.name)
        }
    }

    fun onHoursChanged(input: String) {
        if (input.all { it.isDigit() } && (input.length <= 2)) {
            applyMutation { copy(customHours = input) }
            viewModelScope.launch {
                classRemindersRepository.setHours(input)
            }
        }
    }

    fun onMinutesChanged(input: String) {
        if (input.all { it.isDigit() } && (input.length <= 2)) {
            val minutesInt = input.toIntOrNull()
            val isError = (minutesInt != null) && (minutesInt > 59)
            applyMutation {
                copy(
                    customMinutes = input, isMinutesError = isError
                )
            }
            viewModelScope.launch {
                classRemindersRepository.setMinutes(input)
            }
        }
    }

    fun onDismissInvalidTimeDialog() {
        applyMutation { copy(showInvalidTimeDialog = false) }
    }
}
