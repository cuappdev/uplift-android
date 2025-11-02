package com.cornellappdev.uplift.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.cornellappdev.uplift.util.CLASS_FAVORITES_KEY
import com.cornellappdev.uplift.util.GYM_FAVORITES_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

object PreferencesKeys {
    val ID = stringPreferencesKey("id")
    val USERNAME = stringPreferencesKey("username")
    val NETID = stringPreferencesKey("netId")
    val EMAIL = stringPreferencesKey("email")
    val SKIP = booleanPreferencesKey("skip")
    val FCM_TOKEN = stringPreferencesKey("fcmToken")
    val DECLINED_NOTIFICATION_PERMISSION =
        booleanPreferencesKey("declinedNotificationPermission")
    val CAPACITY_REMINDERS_ID = intPreferencesKey("capacityRemindersId")
    val CAPACITY_REMINDERS_TOGGLE = booleanPreferencesKey("capacityRemindersToggle")
    val CAPACITY_REMINDERS_PERCENT = intPreferencesKey("capacityRemindersPercent")
    val CAPACITY_REMINDERS_SELECTED_DAYS = stringSetPreferencesKey("capacityRemindersSelectedDays")
    val CAPACITY_REMINDERS_SELECTED_GYMS = stringSetPreferencesKey("capacityRemindersSelectedGyms")
    val CAPACITY_REMINDERS_TUTORIAL_SHOWN = booleanPreferencesKey("capacityReminderTutorialShown")
}

@Singleton
class DatastoreRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun <T> storePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun <T> getPreference(key: Preferences.Key<T>): T? {
        return dataStore.data.map { preferences ->
            preferences[key]
        }.firstOrNull()
    }

    suspend fun <T> deletePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    /**
     * A [StateFlow] that emits sets of gym ids. A gym is considered a favorite if it exists
     * in this flow.
     */
    val favoriteGymsFlow = dataStore.data.map { pref ->
        pref[stringSetPreferencesKey(GYM_FAVORITES_KEY)] ?: setOf()
    }.stateIn(
        CoroutineScope(Dispatchers.Main),
        SharingStarted.Eagerly,
        setOf()
    )

    /**
     * A [StateFlow] that emits sets of class ids. A class is considered a favorite if it exists
     * in this flow.
     */
    val favoriteClassesFlow = dataStore.data.map { pref ->
        pref[stringSetPreferencesKey(CLASS_FAVORITES_KEY)] ?: setOf()
    }.stateIn(
        CoroutineScope(Dispatchers.Main),
        SharingStarted.Eagerly,
        setOf()
    )

    /**
     * Asynchronously saves the favorite state of a gym whose id is [id].
     */
    fun saveFavoriteGym(id: String, favorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                val set = favoriteGymsFlow.value.toMutableSet()
                if (favorite) {
                    set.add(id)
                } else {
                    set.remove(id)
                }
                preferences[stringSetPreferencesKey(GYM_FAVORITES_KEY)] = set
            }
        }
    }

    /**
     * Asynchronously saves the favorite state of a class whose id is [id].
     */
    fun saveFavoriteClass(id: String, favorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                val set = favoriteClassesFlow.value.toMutableSet()
                if (favorite) {
                    set.add(id)
                } else {
                    set.remove(id)
                }
                preferences[stringSetPreferencesKey(CLASS_FAVORITES_KEY)] = set
            }
        }
    }

    fun hasShownCapacityTutorial(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.CAPACITY_REMINDERS_TUTORIAL_SHOWN] ?: false
        }
    }
}