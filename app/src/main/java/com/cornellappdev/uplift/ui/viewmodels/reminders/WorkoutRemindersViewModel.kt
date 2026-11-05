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
            val prefs = dataStoreRepository.getPreferencesSnapshot()
            val sliderValue = try {
                prefs[PreferencesKeys.WORKOUT_REMINDERS_SLIDER_VALUE] ?: 1
            } catch (_: Exception) {
                1
            }
            val rawRemindersSet = try {
                prefs[PreferencesKeys.WORKOUT_REMINDERS_REMINDERS] ?: emptySet()
            } catch (_: Exception) {
                emptySet()
            }
            val loadedReminders = rawRemindersSet.mapNotNull { it.toReminder() }

            applyMutation {
                copy(
                    sliderValue = sliderValue,
                    reminders = loadedReminders
                )
            }
        }
    }

    fun onBack() = rootNavigationRepository.navigateUp()

    fun onGoalValueChange(value: Int) {
        applyMutation { copy(sliderValue = value) }
        viewModelScope.launch {
            dataStoreRepository.storePreference(PreferencesKeys.WORKOUT_REMINDERS_SLIDER_VALUE, value)
        }
    }

    fun onRemindersChange(reminders: List<Reminder>) {
        applyMutation { copy(reminders = reminders) }
        saveRemindersToDataStore(reminders)
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
            saveRemindersToDataStore(updatedReminders)
            copy(
                reminders = updatedReminders,
                selectedReminder = null,
                deleteDialogOpen = false,
                addNewReminderState = false
            )
        }
    }

    private fun saveRemindersToDataStore(reminders: List<Reminder>) {
        viewModelScope.launch {
            val stringSet = reminders.map { it.toStorageString() }.toSet()
            dataStoreRepository.storePreference(PreferencesKeys.WORKOUT_REMINDERS_REMINDERS, stringSet)
        }
    }

    companion object {
        fun Reminder.toStorageString(): String = "$time|${days.joinToString(",")}|$enabled"

        fun String.toReminder(): Reminder? {
            val parts = split("|")
            if (parts.size < 3) return null
            val time = parts[0]
            val days = if (parts[1].isEmpty()) emptySet() else parts[1].split(",").toSet()
            val enabled = parts[2].toBooleanStrictOrNull() ?: true
            return Reminder(time, days, enabled)
        }
    }
}