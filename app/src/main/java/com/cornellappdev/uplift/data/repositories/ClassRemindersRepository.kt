package com.cornellappdev.uplift.data.repositories

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRemindersRepository @Inject constructor(
    private val dataStoreRepository: DatastoreRepository
) {
    suspend fun getPreferencesSnapshot() = dataStoreRepository.getPreferencesSnapshot()

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