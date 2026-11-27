package com.cornellappdev.uplift.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * BroadcastReceiver responsible for receiving alarm triggers from AlarmManager,
 * posting local reminder notifications via NotificationHelper.
 */
@AndroidEntryPoint
class LocalReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        // placeholders in case an intent arrives without a body
        val title = intent.getStringExtra(AlarmScheduler.EXTRA_TITLE) ?: "Uplift Reminder"
        val body = intent.getStringExtra(AlarmScheduler.EXTRA_BODY)
            ?: "It's time for your scheduled reminder!"

        notificationHelper.showNotification(title = title, body = body)
    }
}
