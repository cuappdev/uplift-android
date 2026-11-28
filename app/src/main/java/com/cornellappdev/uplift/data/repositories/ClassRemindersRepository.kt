package com.cornellappdev.uplift.data.repositories

import android.content.Context
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRemindersRepository @Inject constructor(
    private val dataStoreRepository: DatastoreRepository,
    private val alarmScheduler: AlarmScheduler,
    @ApplicationContext private val context: Context
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
        val selectedOption = getTimeOption()

        val offsetMillis = when (selectedOption) {
            "THIRTY_MIN" -> 30 * 60 * 1000L
            "SIXTY_MIN" -> 60 * 60 * 1000L
            "CUSTOM" -> {
                val hours = getHours().toIntOrNull() ?: 0
                val minutes = getMinutes().toIntOrNull() ?: 0
                (hours * 60 * 60 * 1000L) + (minutes * 60 * 1000L)
            }
            else -> 30 * 60 * 1000L
        }

        if (offsetMillis <= 0L) {
            cancelClassReminder()
            return
        }

        val scheduledTimeMillisec = System.currentTimeMillis() + offsetMillis
        val title = context.getString(R.string.class_reminder_notification_title)
        val body = context.getString(R.string.class_reminder_notification_body)

        alarmScheduler.scheduleAlarm(
            reminderId = CLASS_REMINDER_ID,
            triggerAtMillis = scheduledTimeMillisec,
            title = title,
            body = body
        )
    }

    suspend fun cancelClassReminder() {
        alarmScheduler.cancelAlarm(CLASS_REMINDER_ID)
    }

    companion object {
        private const val CLASS_REMINDER_ID = 1001 // choose some unique ID
    }
}
