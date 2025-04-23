package com.cornellappdev.uplift.data.repositories

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.CreateCapacityReminderMutation
import com.cornellappdev.uplift.DeleteCapacityReminderMutation
import com.cornellappdev.uplift.EditCapacityReminderMutation
import com.cornellappdev.uplift.data.mappers.toResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapacityRemindersRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dataStoreRepository: DatastoreRepository,
) {
    suspend fun createCapacityReminder(
        capacityPercent: Int,
        daysOfWeek: List<String>,
        gyms: List<String>
    ): Result<CreateCapacityReminderMutation.Data> {
        val fcmToken =
            dataStoreRepository.getPreference(PreferencesKeys.FCM_TOKEN) ?: return Result.failure(
                IllegalStateException("FCM token not found")
            )
        val result = apolloClient.mutation(
            CreateCapacityReminderMutation(
                capacityPercent,
                daysOfWeek,
                fcmToken,
                gyms
            )
        ).execute().toResult()
        if (result.isSuccess) {
            Log.d(
                "CapacityRemindersRepository",
                "Created reminder with ID: ${result.getOrNull()?.createCapacityReminder?.id}"
            )
            val id =
                result.getOrNull()?.createCapacityReminder?.id?.toInt() ?: return Result.failure(
                    IllegalStateException("Failed to get reminder ID")
                )
            dataStoreRepository.storePreference(PreferencesKeys.CAPACITY_REMINDERS_ID, id)
            dataStoreRepository.storePreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE, true)
            dataStoreRepository.storePreference(
                PreferencesKeys.CAPACITY_REMINDERS_PERCENT,
                capacityPercent
            )
            dataStoreRepository.storePreference(
                PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS,
                daysOfWeek.toSet()
            )
            dataStoreRepository.storePreference(
                PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS,
                gyms.toSet()
            )
        } else {
            Log.e("CapacityRemindersRepository", "Failed to create reminder")
            Log.d(
                "CapacityRemindersRepository",
                result.exceptionOrNull()?.message ?: "Unknown error"
            )
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
            Log.d("CapacityRemindersRepository", "Edited reminder with ID: $reminderId")
            dataStoreRepository.storePreference(
                PreferencesKeys.CAPACITY_REMINDERS_PERCENT,
                capacityPercent
            )
            dataStoreRepository.storePreference(
                PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS,
                daysOfWeek.toSet()
            )
            dataStoreRepository.storePreference(
                PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS,
                gyms.toSet()
            )
        } else {
            Log.e("CapacityRemindersRepository", "Failed to edit reminder with ID: $reminderId")
            Log.d(
                "CapacityRemindersRepository",
                result.exceptionOrNull()?.message ?: "Unknown error"
            )
        }
        return result
    }

    suspend fun deleteCapacityReminder(reminderId: Int): Result<DeleteCapacityReminderMutation.Data> {
        val result = apolloClient.mutation(
            DeleteCapacityReminderMutation(reminderId)
        ).execute().toResult()
        if (result.isSuccess) {
            Log.d("CapacityRemindersRepository", "Deleted reminder with ID: $reminderId")
            dataStoreRepository.storePreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE, false)
            dataStoreRepository.deletePreference(PreferencesKeys.CAPACITY_REMINDERS_ID)
        } else {
            Log.e("CapacityRemindersRepository", "Failed to delete reminder with ID: $reminderId")
            Log.d(
                "CapacityRemindersRepository",
                result.exceptionOrNull()?.message ?: "Unknown error"
            )
        }
        return result
    }

    suspend fun updateCapacityReminderOnFCMTokenChange() {
        val capacityReminderId =
            dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_ID) ?: return
        val capacityPercent =
            dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_PERCENT) ?: return
        val selectedDays =
            dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_DAYS)
                ?.toList() ?: return
        val selectedGyms =
            dataStoreRepository.getPreference(PreferencesKeys.CAPACITY_REMINDERS_SELECTED_GYMS)
                ?.toList() ?: return

        deleteCapacityReminder(capacityReminderId)
        createCapacityReminder(
            capacityPercent,
            selectedDays,
            selectedGyms
        )
    }
}