package com.cornellappdev.uplift.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * BroadcastReceiver responsible for receiving alarm triggers from AlarmManager,
 * posting local reminder notifications via NotificationHelper, and scheduling
 * the next weekly recurrence.
 */
@AndroidEntryPoint
class LocalReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        // placeholders in case an intent arrives without a body
        val title = intent.getStringExtra(AlarmScheduler.EXTRA_TITLE) ?: "Uplift Reminder"
        val body = intent.getStringExtra(AlarmScheduler.EXTRA_BODY) ?: "It's time for your scheduled reminder!"
        val reminderId = intent.getIntExtra(AlarmScheduler.EXTRA_REMINDER_ID, -1)

        notificationHelper.showNotification(title = title, body = body)

        // automatically re-schedule this alert for 7 days later to make it recur weekly
        if (reminderId != -1) {
            val nextWeekTime = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
            alarmScheduler.scheduleAlarm(
                reminderId = reminderId,
                triggerAtMillis = nextWeekTime,
                title = title,
                body = body
            )
        }
    }
}
