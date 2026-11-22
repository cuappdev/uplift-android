package com.cornellappdev.uplift.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class LocalReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var datastoreRepository: DatastoreRepository

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_BODY = "extra_body"
        const val EXTRA_REQUEST_CODE = "extra_request_code"
        const val EXTRA_DESTINATION = "extra_destination"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Uplift Reminder"
        val body = intent.getStringExtra(EXTRA_BODY) ?: "It's time for your scheduled reminder!"
        val requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)
        val destination = intent.getStringExtra(EXTRA_DESTINATION) ?: "notification_hub"

        notificationHelper.showLocalReminderNotification(
            title = title,
            body = body,
            notificationId = requestCode,
            destinationRoute = destination
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentJsonStr = datastoreRepository.getPreference(PreferencesKeys.NOTIFICATION_HUB_HISTORY) ?: "[]"
                val jsonArray = JSONArray(currentJsonStr)

                val newItem = JSONObject().apply {
                    put("id", System.currentTimeMillis().toString())
                    put("title", title)
                    put("body", body)
                    put("timestamp", System.currentTimeMillis())
                    put("destinationRoute", destination)
                }

                jsonArray.put(newItem)
                datastoreRepository.storePreference(PreferencesKeys.NOTIFICATION_HUB_HISTORY, jsonArray.toString())
            } catch (e: Exception) {
                // Ignore or log error
            }
        }
    }
}
