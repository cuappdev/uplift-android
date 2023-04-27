package com.cornellappdev.uplift.models

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.cornellappdev.uplift.util.CLASS_FAVORITES_KEY
import com.cornellappdev.uplift.util.GYM_FAVORITES_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DatastoreRepository(val dataStore: DataStore<Preferences>) {
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
}