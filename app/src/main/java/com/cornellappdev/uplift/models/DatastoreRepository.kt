package com.cornellappdev.uplift.models

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.cornellappdev.uplift.util.FAVORITES_KEY
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
    val favoritedGymsFlow = dataStore.data.map { pref ->
        pref[stringSetPreferencesKey(FAVORITES_KEY)] ?: setOf()
    }.stateIn(
        CoroutineScope(Dispatchers.Main),
        SharingStarted.Eagerly,
        setOf()
    )

    /**
     * Asynchronously saves the favorite state of a gym whose id is [id].
     */
    fun saveFavoriteGym(id : String, favorite : Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                val set = favoritedGymsFlow.value.toMutableSet()
                if (favorite) {
                    set.add(id)
                }
                else {
                    set.remove(id)
                }
                preferences[stringSetPreferencesKey(FAVORITES_KEY)] = set
            }
        }
    }
}