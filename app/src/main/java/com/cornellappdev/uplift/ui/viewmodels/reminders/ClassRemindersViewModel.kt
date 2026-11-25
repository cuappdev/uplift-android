package com.cornellappdev.uplift.ui.viewmodels.reminders

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.ClassRemindersRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val classRemindersRepository: ClassRemindersRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<ClassRemindersUiState>(ClassRemindersUiState()) {

    init {
        viewModelScope.launch {
            val prefs = classRemindersRepository.getPreferencesSnapshot()
            val toggledOn = try {
                prefs[PreferencesKeys.CLASS_REMINDERS_TOGGLE] ?: false
            } catch (_: Exception) {
                false
            }
            val timeOptionStr = try {
                prefs[PreferencesKeys.CLASS_REMINDERS_TIME_OPTION]
            } catch (_: Exception) {
                null
            }
            val hours = try {
                prefs[PreferencesKeys.CLASS_REMINDERS_HOURS] ?: ""
            } catch (_: Exception) {
                ""
            }
            val minutes = try {
                prefs[PreferencesKeys.CLASS_REMINDERS_MINUTES] ?: ""
            } catch (_: Exception) {
                ""
            }

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

    fun onBack() = rootNavigationRepository.navigateUp()

    fun setToggle(toggledOn: Boolean) {
        applyMutation { copy(toggledOn = toggledOn) }
        viewModelScope.launch {
            classRemindersRepository.setToggle(toggledOn)
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
                    customMinutes = input,
                    isMinutesError = isError
                )
            }
            viewModelScope.launch {
                classRemindersRepository.setMinutes(input)
            }
        }
    }
}