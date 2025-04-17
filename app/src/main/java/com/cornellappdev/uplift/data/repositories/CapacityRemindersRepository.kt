package com.cornellappdev.uplift.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.CreateCapacityReminderMutation
import com.cornellappdev.uplift.DeleteCapacityReminderMutation
import com.cornellappdev.uplift.EditCapacityReminderMutation
import com.cornellappdev.uplift.data.mappers.toResult
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapacityRemindersRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun createCapacityReminder(
        capacityPercent: Int,
        daysOfWeek: List<String>,
        fcmToken: String,
        gyms: List<String>
    ): Result<CreateCapacityReminderMutation.Data> {
        val result = apolloClient.mutation(
            CreateCapacityReminderMutation(
                capacityPercent,
                daysOfWeek,
                fcmToken,
                gyms
            )
        ).execute().toResult()
        if (result.isSuccess) {
            val id = result.getOrNull()?.createCapacityReminder?.id?.toInt() ?: -1
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_ID, id)
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE, true)
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_PERCENT, capacityPercent)
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS, daysOfWeek.toSet())
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS, gyms.toSet())
        }
        return result
    }

    suspend fun editCapacityReminder(
        reminderId: Int,
        capacityPercent: Int,
        daysOfWeek: List<String>,
        gyms: List<String>
    ): Result<EditCapacityReminderMutation.Data> {
        val result = apolloClient.mutation(
            EditCapacityReminderMutation(
                reminderId,
                capacityPercent,
                daysOfWeek,
                gyms
            )
        ).execute().toResult()
        if (result.isSuccess) {
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_PERCENT, capacityPercent)
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS, daysOfWeek.toSet())
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS, gyms.toSet())
        }
        return result
    }

    suspend fun deleteCapacityReminder(reminderId: Int): Result<DeleteCapacityReminderMutation.Data> {
        val result = apolloClient.mutation(
            DeleteCapacityReminderMutation(reminderId)
        ).execute().toResult()
        if (result.isSuccess) {
            deletePreference(PreferencesKeys.CAPACITY_REMINDERS_ID)
            deletePreference(PreferencesKeys.CAPACITY_REMINDERS_PERCENT)
            deletePreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS)
            deletePreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS)
            storePreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE, false)
        }
        return result
    }

    suspend fun <T> storePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    private suspend fun <T> getPreference(key: Preferences.Key<T>): T? {
        return dataStore.data.map { preferences ->
            preferences[key]
        }.firstOrNull()
    }

    private suspend fun <T> deletePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}