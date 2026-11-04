package com.cornellappdev.uplift.ui.viewmodels.reminders

import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.screens.reminders.Reminder
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WorkoutRemindersUiState(
    val sliderValue: Float = 1f,
    val reminders: List<Reminder> = emptyList(),
    val selectedReminder: Reminder? = null,
    val addNewReminderState: Boolean = false,
    val deleteDialogOpen: Boolean = false,
)

@HiltViewModel
class WorkoutRemindersViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<WorkoutRemindersUiState>(WorkoutRemindersUiState()) {

    fun onBack() = rootNavigationRepository.navigateUp()

    fun onGoalValueChange(value: Float) {
        applyMutation { copy(sliderValue = value) }
    }

    fun onRemindersChange(reminders: List<Reminder>) {
        applyMutation { copy(reminders = reminders) }
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
            copy(
                reminders = if (selectedReminder != null) reminders.filter { it != selectedReminder } else reminders,
                selectedReminder = null,
                deleteDialogOpen = false,
                addNewReminderState = false
            )
        }
    }
}