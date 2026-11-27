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

    suspend fun scheduleClassReminder() {
        // TODO: Fix 0 hours and 0 seconds issue (Temp fix: if it is 0 hrs 0 mins, it will not run.)
        val hours = getHours().toIntOrNull() ?: 0
        val minutes = getMinutes().toIntOrNull() ?: 0

        val validMinutes = if (minutes >= 0 && minutes <= 60) minutes else 0 // default to 0 if invalid
        val validHours = if (hours >= 0) hours else 0 // default to 0 if invalid

        val offsetMillis = (validHours * 60 * 60 * 1000L) + (validMinutes * 60 * 1000L)
        val scheduledTimeMillisec = System.currentTimeMillis() + if (offsetMillis > 0) offsetMillis else (24 * 60 * 60 * 1000L)

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
