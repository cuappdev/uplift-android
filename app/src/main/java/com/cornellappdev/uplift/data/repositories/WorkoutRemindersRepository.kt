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
        updateAlarms(reminders)
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

    private fun updateAlarms(reminders: List<Reminder>) {
        reminders.forEachIndexed { index, reminder ->
            val reminderId = WORKOUT_REMINDER_ID_BASE + index
            if (reminder.enabled) {
                val scheduledTimeMillisec = System.currentTimeMillis() + 5000L // dummy time for testing
                alarmScheduler.scheduleAlarm(
                    reminderId = reminderId,
                    triggerAtMillis = scheduledTimeMillisec,
                    title = "Workout Reminder",
                    body = "Time for your scheduled workout!"
                )
            } else {
                alarmScheduler.cancelAlarm(reminderId)
            }
        }
    }

    companion object {
        private const val WORKOUT_REMINDER_ID_BASE = 2000
    }
}
