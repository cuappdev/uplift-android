package com.cornellappdev.uplift.data.repositories

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
        }
        return result
    }

    suspend fun deleteCapacityReminder(reminderId: Int): Result<DeleteCapacityReminderMutation.Data> {
        val result = apolloClient.mutation(
            DeleteCapacityReminderMutation(reminderId)
        ).execute().toResult()
        if (result.isSuccess) {
            dataStoreRepository.storePreference(PreferencesKeys.CAPACITY_REMINDERS_TOGGLE, false)
            dataStoreRepository.deletePreference(PreferencesKeys.CAPACITY_REMINDERS_ID)
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