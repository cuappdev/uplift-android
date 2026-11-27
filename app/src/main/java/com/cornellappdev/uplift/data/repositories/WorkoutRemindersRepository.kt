package com.cornellappdev.uplift.data.repositories

import com.cornellappdev.uplift.ui.screens.reminders.Reminder
import com.cornellappdev.uplift.util.AlarmScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRemindersRepository @Inject constructor(
    private val dataStoreRepository: DatastoreRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend fun getWorkoutReminders(): List<Reminder> {
        return dataStoreRepository.getWorkoutReminders()
    }

    suspend fun saveWorkoutReminders(reminders: List<Reminder>) {
        dataStoreRepository.saveWorkoutReminders(reminders)
        // TODO: Schedule/update alarms for reminders
    }

    suspend fun getSliderValue(): Int {
        return try {
            dataStoreRepository.getPreference(PreferencesKeys.WORKOUT_REMINDERS_SLIDER_VALUE) ?: 1
        } catch (_: Exception) {
            1
        }
    }

    suspend fun saveSliderValue(value: Int) {
        dataStoreRepository.storePreference(PreferencesKeys.WORKOUT_REMINDERS_SLIDER_VALUE, value)
    }
}
