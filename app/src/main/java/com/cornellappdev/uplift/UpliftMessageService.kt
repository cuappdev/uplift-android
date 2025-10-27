package com.cornellappdev.uplift

import android.util.Log
import com.cornellappdev.uplift.data.repositories.CapacityRemindersRepository
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpliftMessageService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var capacityRemindersRepository: CapacityRemindersRepository

    @Inject
    lateinit var datastoreRepository: DatastoreRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            datastoreRepository.storePreference(
                key = PreferencesKeys.FCM_TOKEN,
                value = token
            )
            capacityRemindersRepository.updateCapacityReminderOnFCMTokenChange()
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(
            "UpliftMessageService",
            "From: ${remoteMessage.from}"
        )
        remoteMessage.notification?.let { notification ->
            Log.d(
                "UpliftMessageService",
                "Message Notification Body: ${notification.body}"
            )
            notificationHelper.showNotification(
                title = notification.title.toString(),
                body = notification.body.toString(),
                data = remoteMessage.data
            )
        }
    }

}