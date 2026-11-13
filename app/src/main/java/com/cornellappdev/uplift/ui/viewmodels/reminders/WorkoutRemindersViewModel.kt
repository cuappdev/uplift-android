package com.cornellappdev.uplift.ui.viewmodels.reminders

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.screens.reminders.Reminder
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutRemindersUiState(
    val sliderValue: Int = 1,
    val reminders: List<Reminder> = emptyList(),
    val selectedReminder: Reminder? = null,
    val addNewReminderState: Boolean = false,
    val deleteDialogOpen: Boolean = false,
)

@HiltViewModel
class WorkoutRemindersViewModel @Inject constructor(
    private val dataStoreRepository: DatastoreRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<WorkoutRemindersUiState>(WorkoutRemindersUiState()) {

    init {
        viewModelScope.launch {
            val sliderValue = try {
                dataStoreRepository.getPreference(PreferencesKeys.WORKOUT_REMINDERS_SLIDER_VALUE) ?: 1
            } catch (_: Exception) {
                1
            }
            val loadedReminders = dataStoreRepository.getWorkoutReminders()

            applyMutation {
                copy(
                    sliderValue = sliderValue,
                    reminders = loadedReminders
                )
            }
        }
    }

    fun onBack() = rootNavigationRepository.navigateUp()

    fun onRemindersChange(reminders: List<Reminder>) {
        applyMutation { copy(reminders = reminders) }
        viewModelScope.launch {
            dataStoreRepository.saveWorkoutReminders(reminders)
        }
    }

    fun onSelectedReminderChange(reminder: Reminder?) {
        applyMutation { copy(selectedReminder = reminder) }
    }

    fun onAddNewReminderStateChange(isAdding: Boolean) {
        applyMutation { copy(addNewReminderState = isAdding) }
    }

    fun onDeleteDialogOpenChange(isOpen: Boolean) {
        applyMutation { copy(deleteDialogOpen = isOpen) }
    }

    fun onDeleteConfirmed() {
        applyMutation {
            val updatedReminders = if (selectedReminder != null) reminders.filter { it != selectedReminder } else reminders
            viewModelScope.launch {
                dataStoreRepository.saveWorkoutReminders(updatedReminders)
            }
            copy(
                reminders = updatedReminders,
                selectedReminder = null,
                deleteDialogOpen = false,
                addNewReminderState = false
            )
        }
    }
}