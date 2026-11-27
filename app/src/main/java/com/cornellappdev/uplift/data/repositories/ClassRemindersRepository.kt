package com.cornellappdev.uplift.data.repositories

import com.cornellappdev.uplift.util.AlarmScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRemindersRepository @Inject constructor(
    private val dataStoreRepository: DatastoreRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend fun getToggle(): Boolean {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_TOGGLE) ?: false
    }

    suspend fun getTimeOption(): String? {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_TIME_OPTION)
    }

    suspend fun getHours(): String {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_HOURS) ?: ""
    }

    suspend fun getMinutes(): String {
        return dataStoreRepository.getPreference(PreferencesKeys.CLASS_REMINDERS_MINUTES) ?: ""
    }

    suspend fun setToggle(toggledOn: Boolean) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_TOGGLE, toggledOn)
    }

    suspend fun setTimeOption(optionName: String) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_TIME_OPTION, optionName)
    }

    suspend fun setHours(hours: String) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_HOURS, hours)
    }

    suspend fun setMinutes(minutes: String) {
        dataStoreRepository.storePreference(PreferencesKeys.CLASS_REMINDERS_MINUTES, minutes)
    }
}
