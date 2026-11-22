package com.cornellappdev.uplift.data.repositories

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.CreateCapacityReminderMutation
import com.cornellappdev.uplift.DeleteCapacityReminderMutation
import com.cornellappdev.uplift.EditCapacityReminderMutation
import com.cornellappdev.uplift.data.mappers.toResult
import com.cornellappdev.uplift.data.notifications.AlarmScheduler
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class CapacityRemindersRepository @Inject constructor(
    @Named("main") private val apolloClient: ApolloClient,
    private val dataStoreRepository: DatastoreRepository,
    private val alarmScheduler: AlarmScheduler
) {
    private fun calculateNextTriggerTime(daysOfWeek: List<String>): Long {
        if (daysOfWeek.isEmpty()) return System.currentTimeMillis() + 24 * 60 * 60 * 1000L

        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        fun dayNameToCalendarDay(name: String): Int? {
            return when (name.uppercase()) {
                "SUNDAY" -> Calendar.SUNDAY
                "MONDAY" -> Calendar.MONDAY
                "TUESDAY" -> Calendar.TUESDAY
                "WEDNESDAY" -> Calendar.WEDNESDAY
                "THURSDAY" -> Calendar.THURSDAY
                "FRIDAY" -> Calendar.FRIDAY
                "SATURDAY" -> Calendar.SATURDAY
                else -> null
            }
        }

        val targetDays = daysOfWeek.mapNotNull { dayNameToCalendarDay(it) }.toSet()
        if (targetDays.isEmpty()) return System.currentTimeMillis() + 24 * 60 * 60 * 1000L

        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        for (i in 0..7) {
            val checkDay = ((currentDayOfWeek - 1 + i) % 7) + 1
            if (targetDays.contains(checkDay)) {
                if (i == 0 && calendar.timeInMillis <= System.currentTimeMillis()) {
                    continue
                }
                if (i > 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, i)
                }
                return calendar.timeInMillis
            }
        }

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.timeInMillis
    }

    /**
     * Creates a capacity reminder with the given parameters and stores the reminder values to preferences.
     * @param capacityPercent The capacity percentage for the reminder (Should be in the range of 0-100).
     * @param daysOfWeek The days of the week for the reminder (Each day of week is all capitalized eg. MONDAY).
     * @param gyms The gyms for the reminder (Each gym is all capitalized with no spaces eg. TEAGLEUP).
     * @return A Result containing the created reminder data (see graphql file) or an error.
     */
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
            val triggerTime = calculateNextTriggerTime(daysOfWeek)
            alarmScheduler.scheduleAlarm(
                requestCode = id,
                triggerAtMillis = triggerTime,
                title = "Gym Capacity Alert",
                body = "Capacity has reached $capacityPercent% at your selected gyms!"
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

    /**
     * Edits an existing capacity reminder with the given parameters and updates the stored reminder values to preferences.
     * @param reminderId The ID of the reminder to edit.
     * @param capacityPercent The new capacity percentage for the reminder (Should be in the range of 0-100).
     * @param daysOfWeek The new days of the week for the reminder (Each day of week is all capitalized eg. MONDAY).
     * @param gyms The new gyms for the reminder (Each gym is all capitalized with no spaces eg. TEAGLEUP).
     * @return A Result containing the edited reminder data (see graphql file) or an error.
     */
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
            val triggerTime = calculateNextTriggerTime(daysOfWeek)
            alarmScheduler.scheduleAlarm(
                requestCode = reminderId,
                triggerAtMillis = triggerTime,
                title = "Gym Capacity Alert",
                body = "Capacity has reached $capacityPercent% at your selected gyms!"
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

    /**
     * Deletes a capacity reminder with the given ID, deletes ID from datastore and updates toggle in datastore.
     * @param reminderId The ID of the reminder to delete.
     * @return A Result containing the deleted reminder data (see graphql file) or an error.
     */
    suspend fun deleteCapacityReminder(reminderId: Int): Result<DeleteCapacityReminderMutation.Data> {
        val result = apolloClient.mutation(
            DeleteCapacityReminderMutation(reminderId)
        ).execute().toResult()
        if (result.isSuccess) {
            alarmScheduler.cancelAlarm(reminderId)
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

    /**
     * Deletes the current existing capacity reminder and creates a new one with the current values
     * in the datastore and the new fcm token.
     */
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
