package com.cornellappdev.uplift.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver responsible for receiving alarm triggers from AlarmManager,
 * posting local reminder notifications, and scheduling subsequent occurrences.
 */
class LocalReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

    }
}
