package com.cornellappdev.uplift.ui.viewmodels.reminders

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.CapacityRemindersRepository
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CapacityRemindersUiState(
    val toggledOn: Boolean = false,
    val selectedDays: Set<String> = emptySet(),
    val capacityThreshold: Float = 0.5f,
    val selectedGyms: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val showUnsavedChangesDialog: Boolean = false
)

@HiltViewModel
class CapacityRemindersViewModel @Inject constructor(
    private val capacityRemindersRepository: CapacityRemindersRepository,
    private val dataStoreRepository: DatastoreRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<CapacityRemindersUiState>(CapacityRemindersUiState()) {

    private var initialState: CapacityRemindersUiState? = null

    init {
        viewModelScope.launch {
            applyMutation { copy(isLoading = true) }

            try {
                val capacityPercent =
                    dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_PERCENT)
                val selectedDays =
                    dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS)
                val selectedGyms =
                    dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS)
                val toggledOn =
                    dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE)

                val newState = CapacityRemindersUiState(
                    toggledOn = toggledOn == true,
                    selectedDays = selectedDays?.map { it.toAbbreviatedDayOfWeek() }?.toSet()
                        ?: emptySet(),
                    capacityThreshold = (capacityPercent ?: 0) / 100f,
                    selectedGyms = selectedGyms?.map { it.toFormattedGymName() }?.toSet()
                        ?: emptySet(),
                    isLoading = false
                )

                initialState = newState
                applyMutation { newState }
            } catch (e: Exception) {
                applyMutation {
                    copy(
                        isLoading = false,
                        error = "Failed to load reminder settings"
                    )
                }
                Log.e(
                    "CapacityRemindersViewModel",
                    "Failed to load reminder settings: ${e.message}"
                )
            }
        }
    }

    fun setToggle(toggledOn: Boolean) {
        applyMutation {
            copy(
                toggledOn = toggledOn,
                hasUnsavedChanges = true,
                saveSuccess = false
            )
        }
    }

    fun setSelectedDays(days: Set<String>) {
        applyMutation {
            copy(
                selectedDays = days,
                hasUnsavedChanges = true,
                saveSuccess = false
            )
        }
    }

    fun setCapacityThreshold(threshold: Float) {
        applyMutation {
            copy(
                capacityThreshold = threshold,
                hasUnsavedChanges = true,
                saveSuccess = false
            )
        }
    }

    fun setSelectedGyms(gyms: Set<String>) {
        applyMutation {
            copy(
                selectedGyms = gyms,
                hasUnsavedChanges = true,
                saveSuccess = false
            )
        }
    }

    fun onBack() {
        if (getStateValue().hasUnsavedChanges) {
            applyMutation { copy(showUnsavedChangesDialog = true) }
        } else {
            rootNavigationRepository.navigateUp()
        }
    }

    fun onConfirmDiscard() {
        applyMutation { copy(showUnsavedChangesDialog = false) }
        rootNavigationRepository.navigateUp()
    }

    fun onDismissDialog() {
        applyMutation { copy(showUnsavedChangesDialog = false) }
    }

    fun saveChanges() {
        applyMutation { copy(isLoading = true, error = null, saveSuccess = false) }
        val currentState = getStateValue()

        if (!currentState.toggledOn) {
            deleteReminder()
            return
        }

        if (currentState.selectedDays.isEmpty() || currentState.selectedGyms.isEmpty()) {
            applyMutation { copy(
                isLoading = false,
                hasUnsavedChanges = true,
                saveSuccess = false,
                error = "Please select at least one day and one gym"
            )
            }
            return
        }

        viewModelScope.launch {
            applyMutation { copy(showUnsavedChangesDialog = false) }
            try {
                val capacityReminderId =
                    dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_ID)
                val capacityPercent = (currentState.capacityThreshold * 100).toInt()
                val daysOfWeek = currentState.selectedDays.map { it.toFullDayOfWeek() }
                val gymNames = currentState.selectedGyms.map { it.uppercase().replace(" ", "") }

                val result = if (capacityReminderId != null) {
                    capacityRemindersRepository.editCapacityReminder(
                        capacityReminderId,
                        capacityPercent,
                        daysOfWeek,
                        gymNames
                    )
                } else {
                    capacityRemindersRepository.createCapacityReminder(
                        capacityPercent,
                        daysOfWeek,
                        gymNames
                    )
                }

                if (result.isSuccess) {
                    initialState = currentState.copy(
                        hasUnsavedChanges = false,
                        isLoading = false,
                        saveSuccess = true
                    )
                    applyMutation {
                        copy(
                            hasUnsavedChanges = false,
                            isLoading = false,
                            saveSuccess = true
                        )
                    }
                } else {
                    applyMutation {
                        copy(
                            isLoading = false,
                            error = "Failed to save reminder. Please try again later.",
                            saveSuccess = false
                        )
                    }
                    Log.e(
                        "CapacityRemindersViewModel",
                        "Failed to save reminder: ${result.exceptionOrNull()?.message ?: "Unknown error"}"
                    )
                }
            } catch (e: Exception) {
                applyMutation {
                    copy(
                        isLoading = false,
                        error = "Error saving reminder: ${e.message}",
                        saveSuccess = false
                    )
                }
                Log.e(
                    "CapacityRemindersViewModel",
                    "Error saving reminder. Please try again later."
                )
            }
        }
    }

    private fun deleteReminder() {
        viewModelScope.launch {
            applyMutation { copy(isLoading = true, error = null, saveSuccess = false) }

            try {
                val capacityReminderId =
                    dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_ID)

                if (capacityReminderId != null) {
                    val result =
                        capacityRemindersRepository.deleteCapacityReminder(capacityReminderId)

                    if (result.isSuccess) {
                        initialState = getStateValue().copy(
                            hasUnsavedChanges = false,
                            isLoading = false,
                            saveSuccess = true
                        )
                        applyMutation {
                            copy(
                                hasUnsavedChanges = false,
                                isLoading = false,
                                saveSuccess = true
                            )
                        }
                    } else {
                        applyMutation {
                            copy(
                                isLoading = false,
                                error = "Failed to delete reminder. Please try again later.",
                            )
                        }
                        Log.e(
                            "CapacityRemindersViewModel",
                            "Failed to delete reminder: ${result.exceptionOrNull()?.message ?: "Unknown error"}"
                        )
                    }
                } else {
                    // No reminder exists to delete, just update the UI state
                    dataStoreRepository.storePreference(
                        PreferencesKeys.CAPACITY_REMINDERS_TOGGLE,
                        false
                    )
                    initialState = getStateValue().copy(
                        hasUnsavedChanges = false,
                        isLoading = false
                    )
                    applyMutation {
                        copy(
                            hasUnsavedChanges = false,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                applyMutation {
                    copy(
                        isLoading = false,
                        error = "Error deleting reminder",
                        saveSuccess = false
                    )
                }
                Log.e(
                    "CapacityRemindersViewModel",
                    "Error deleting reminder: ${e.message}"
                )
            }
        }
    }

    private fun String.toFullDayOfWeek(): String {
        return when (this) {
            "M" -> "MONDAY"
            "T" -> "TUESDAY"
            "W" -> "WEDNESDAY"
            "Th" -> "THURSDAY"
            "F" -> "FRIDAY"
            "Sa" -> "SATURDAY"
            else -> "SUNDAY"
        }
    }

    private fun String.toAbbreviatedDayOfWeek(): String {
        return when (this) {
            "MONDAY" -> "M"
            "TUESDAY" -> "T"
            "WEDNESDAY" -> "W"
            "THURSDAY" -> "Th"
            "FRIDAY" -> "F"
            "SATURDAY" -> "Sa"
            else -> "Su"
        }
    }

    private fun String.toFormattedGymName(): String {
        return when (this) {
            "TEAGLEUP" -> "Teagle Up"
            "TEAGLEDOWN" -> "Teagle Down"
            "HELENNEWMAN" -> "Helen Newman"
            "NOYES" -> "Noyes"
            else -> "Toni Morrison"
        }
    }
}