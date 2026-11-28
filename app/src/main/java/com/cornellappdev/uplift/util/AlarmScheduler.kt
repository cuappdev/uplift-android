package com.cornellappdev.uplift.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class responsible for scheduling and canceling exact local alarms
 * using Android's AlarmManager.
 */
@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Schedules an exact alarm that fires at [triggerAtMillis] and triggers [LocalReminderReceiver].
     */
    fun scheduleAlarm(
        reminderId: Int,
        triggerAtMillis: Long,
        title: String,
        body: String
    ) {
        val intent = Intent(context, LocalReminderReceiver::class.java).apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_BODY, body)
            putExtra(EXTRA_REMINDER_ID, reminderId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                return
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // catch if exact alarm permissions/restrictions apply
        }
    }

    /**
     * Cancels a previously scheduled alarm matching [reminderId].
     */
    fun cancelAlarm(reminderId: Int) {
        val intent = Intent(context, LocalReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Cancels all scheduled local alarms on user logout.
     */
    fun cancelAllAlarms() {
        // TODO: clear active alarms when user logs out.
    }

    companion object {
        // EXTRA_... follows android naming conventions for Intent
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_BODY = "extra_body"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
    }
}
