package com.cornellappdev.uplift.data.repositories

import com.cornellappdev.uplift.util.AlarmScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRemindersRepository @Inject constructor(
    private val dataStoreRepository: DatastoreRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend fun getToggle(): Boolean {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_TOGGLE) ?: false
    }

    suspend fun getTimeOption(): String? {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_TIME_OPTION)
    }

    suspend fun getHours(): String {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_HOURS) ?: ""
    }

    suspend fun getMinutes(): String {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_MINUTES) ?: ""
    }

    suspend fun setToggle(toggledOn: Boolean) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_TOGGLE, toggledOn)
    }

    suspend fun setTimeOption(optionName: String) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_TIME_OPTION, optionName)
    }

    suspend fun setHours(hours: String) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_HOURS, hours)
    }

    suspend fun setMinutes(minutes: String) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_MINUTES, minutes)
    }

    /**
     * Schedules a local class reminder using AlarmScheduler.
     */
    suspend fun scheduleClassReminder() {
        // TODO: replace scheduledTimeMillisec with actual user input
        val scheduledTimeMillisec = System.currentTimeMillis() + 24 * 60 * 60 * 1000

        alarmScheduler.scheduleAlarm(
            reminderId = CLASS_REMINDER_ID,
            triggerAtMillis = scheduledTimeMillisec,
            title = "Class Reminder",
            body = "You have an upcoming class soon!"
        )
    }

    /**
     * Cancels the scheduled class reminder.
     */
    suspend fun cancelClassReminder() {
        alarmScheduler.cancelAlarm(CLASS_REMINDER_ID)
    }

    companion object {
        private const val CLASS_REMINDER_ID = 1001 // choose some unique ID
    }
}
