package com.cornellappdev.uplift.ui.viewmodels.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: Long,
    val destinationRoute: String
)

@HiltViewModel
class NotificationHubViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    val notifications: StateFlow<List<NotificationItem>> = datastoreRepository.notificationHistoryFlow
        .map { jsonStr ->
            try {
                val array = JSONArray(jsonStr)
                val list = mutableListOf<NotificationItem>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        NotificationItem(
                            id = obj.optString("id", i.toString()),
                            title = obj.optString("title", "Uplift Reminder"),
                            body = obj.optString("body", ""),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                            destinationRoute = obj.optString("destinationRoute", "notification_hub")
                        )
                    )
                }
                list.sortedByDescending { it.timestamp }
            } catch (e: Exception) {
                emptyList()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun clearHistory() {
        viewModelScope.launch {
            datastoreRepository.storePreference(PreferencesKeys.NOTIFICATION_HUB_HISTORY, "[]")
        }
    }
}
