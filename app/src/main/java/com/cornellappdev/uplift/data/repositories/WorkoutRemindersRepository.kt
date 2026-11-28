package com.cornellappdev.uplift.data.repositories

import android.content.Context
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.screens.reminders.Reminder
import com.cornellappdev.uplift.util.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRemindersRepository @Inject constructor(
    private val dataStoreRepository: DatastoreRepository,
    private val alarmScheduler: AlarmScheduler,
    @ApplicationContext private val context: Context
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

    private fun updateAlarms(reminders: List<Reminder>) {
        reminders.forEachIndexed { index, reminder -> // there can be multiple reminders
            val reminderId = WORKOUT_REMINDER_ID_BASE + index
            if (reminder.enabled) {
                val scheduledTimeMillisec = calculateNotificationTime(reminder)
                    ?: (System.currentTimeMillis() + 24 * 60 * 60 * 1000L) // in case it is null
                val title = context.getString(R.string.workout_reminder_notification_title)
                val body =
                    context.getString(R.string.workout_reminder_notification_body, reminder.time)
                alarmScheduler.scheduleAlarm(
                    reminderId = reminderId,
                    triggerAtMillis = scheduledTimeMillisec,
                    title = title,
                    body = body
                )
            } else {
                alarmScheduler.cancelAlarm(reminderId)
            }
        }
    }

    private fun calculateNotificationTime(reminder: Reminder): Long? {
        try { // try catch block for string splitting
            val userTimeInput = reminder.time.split(" ")
            if (userTimeInput.size != 2) return null
            val hoursAndMinutes = userTimeInput[0].split(":")
            if (hoursAndMinutes.size != 2) return null

            var hour = hoursAndMinutes[0].toIntOrNull() ?: return null
            val minute = hoursAndMinutes[1].toIntOrNull() ?: return null
            val isPM = userTimeInput[1].equals("PM", ignoreCase = true)

            if (isPM && hour < 12) {
                hour += 12 // Calendar uses military time (24 hr)
            } else if (!isPM && hour == 12) {
                hour = 0
            }

            val calendar = java.util.Calendar.getInstance()
            val now = calendar.timeInMillis

            for (dayOffset in 0..7) { // check if the user chose this date (0=today, 1=tomorrow...)
                calendar.timeInMillis = now
                calendar.add(java.util.Calendar.DAY_OF_YEAR, dayOffset)
                calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
                calendar.set(java.util.Calendar.MINUTE, minute)
                calendar.set(java.util.Calendar.SECOND, 0)
                calendar.set(java.util.Calendar.MILLISECOND, 0)

                val candidateTime = calendar.timeInMillis
                if (candidateTime <= now) continue

                if (isMatchingDay(calendar, reminder.days)) {
                    return candidateTime
                }
            }
        } catch (_: Exception) {
        }
        return null
    }

    private fun isMatchingDay(calendar: java.util.Calendar, reminderDays: Set<String>): Boolean {
        val dayAbbr = when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            java.util.Calendar.MONDAY -> "M"
            java.util.Calendar.TUESDAY -> "T"
            java.util.Calendar.WEDNESDAY -> "W"
            java.util.Calendar.THURSDAY -> "Th"
            java.util.Calendar.FRIDAY -> "F"
            java.util.Calendar.SATURDAY -> "Sa"
            java.util.Calendar.SUNDAY -> "Su"
            else -> ""
        }
        return reminderDays.contains(dayAbbr)
    }

    companion object {
        private const val WORKOUT_REMINDER_ID_BASE = 5000
    }
}
