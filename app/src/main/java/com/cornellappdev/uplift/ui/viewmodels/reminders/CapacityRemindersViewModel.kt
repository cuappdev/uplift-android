package com.cornellappdev.uplift.ui.viewmodels.reminders

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.CapacityRemindersRepository
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.ui.UpliftRootRoute
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
)

@HiltViewModel
class CapacityRemindersViewModel @Inject constructor(
    private val capacityRemindersRepository: CapacityRemindersRepository,
    private val dataStoreRepository: DatastoreRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<CapacityRemindersUiState>(CapacityRemindersUiState()) {

    init {
        viewModelScope.launch {
            Log.d(
                "CAPACITY_REMINDERS",
                dataStoreRepository.getPreference(PreferencesKeys.FCM_TOKEN).toString()
            )
            val capacityPercent =
                dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_PERCENT)
            val selectedDays =
                dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS)
            val selectedGyms =
                dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS)
            val toggledOn =
                dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE)

            applyMutation {
                copy(
                    toggledOn = toggledOn == true,
                    selectedDays = selectedDays?.map { it.toAbbreviatedDayOfWeek() }?.toSet()
                        ?: emptySet(),
                    capacityThreshold = (capacityPercent ?: 0) / 100f,
                    selectedGyms = selectedGyms?.map { it.toFormattedGymName() }?.toSet()
                        ?: emptySet()
                )
            }
        }

    }

    fun setToggle(toggledOn: Boolean) {
        applyMutation { copy(toggledOn = toggledOn) }
        viewModelScope.launch {
            val capacityReminderId =
                dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_ID)
            if (toggledOn && capacityReminderId == null) {
                val capacityReminder = getStateValue()
                val capacityPercent = (capacityReminder.capacityThreshold * 100).toInt()
                val daysOfWeek = capacityReminder.selectedDays.map { it.toFullDayOfWeek() }
                val gymNames = capacityReminder.selectedGyms.map { it.uppercase().replace(" ", "") }
                capacityRemindersRepository.createCapacityReminder(
                    capacityPercent,
                    daysOfWeek,
                    gymNames
                )
            } else if (!toggledOn && capacityReminderId != null) {
                capacityRemindersRepository.deleteCapacityReminder(capacityReminderId)
            }
        }
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

    fun onBack() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }

    fun saveChanges() {
        viewModelScope.launch {
            val capacityRemindersId =
                dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_ID)
            val capacityReminder = getStateValue()
            val capacityPercent = (capacityReminder.capacityThreshold * 100).toInt()
            val daysOfWeek = capacityReminder.selectedDays.map { it.toFullDayOfWeek() }
            val gymNames = capacityReminder.selectedGyms.map { it.uppercase().replace(" ", "") }
            if (capacityRemindersId != null) {
                capacityRemindersRepository.editCapacityReminder(
                    capacityRemindersId,
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

