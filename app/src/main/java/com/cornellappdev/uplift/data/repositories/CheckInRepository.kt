package com.cornellappdev.uplift.data.repositories

import android.location.Location
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.util.getDistanceBetween
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.LogWorkoutMutation
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInRepository @Inject constructor(
    val upliftApiRepository: UpliftApiRepository,
    private val dataStore: DataStore<Preferences>,
    private val apolloClient: ApolloClient,
    private val userInfoRepository: UserInfoRepository
    ){
    private val _nearestGymFlow = MutableStateFlow<UpliftGym?>(null)
    val nearestGymFlow: StateFlow<UpliftGym?> = _nearestGymFlow.asStateFlow()

    private val proximityMiles = 0.05f

    /**
     * Evaluates the user's proximity to all gyms and updates [_nearestGymFlow] with the closest gym
     * if it is within [proximityMiles]. Otherwise sets it to null.
     */
    fun evaluateProximity(location: Location) {
        val gymResponse = upliftApiRepository.gymApiFlow.value
        if (gymResponse is ApiResponse.Success) {
            val gyms = gymResponse.data
            val nearestGym = gyms.minByOrNull { gym ->
                getDistanceBetween(
                    location.latitude,
                    location.longitude,
                    gym.latitude,
                    gym.longitude
                )
            }
            nearestGym?.let { gym ->
                val distance = getDistanceBetween(
                    location.latitude,
                    location.longitude,
                    gym.latitude,
                    gym.longitude
                )
                if (distance <= proximityMiles) {
                    _nearestGymFlow.update { gym }
                } else {
                    _nearestGymFlow.update { null }
                }
            }
        } else {
            _nearestGymFlow.update { null }
        }
    }

    /**
     * Formats a given timestamps [ms] into a human-readable string for display on the check in feature
     */
    fun formatTime(ms: Long): String =
        SimpleDateFormat("h:mm a", Locale.US).apply{
            timeZone = TimeZone.getTimeZone("America/New_York")
        }.format(Date(ms))


    private val KEY_CHECKIN_SUPPRESS_UNTIL = longPreferencesKey("checkin_suppress_until")
    private val KEY_CHECKIN_LAST_DATE = stringPreferencesKey("checkin_last_date")
    private val zone: ZoneId = ZoneId.systemDefault()

    /**
     * A [StateFlow] indicating whether the check-in prompt should be shown. Based on dismiss
     * cooldown and whether the user has already checked in today.
     *
     * Note: Since DataStore only emits when data changes, this flow isn't automatically re-evaluated
     * after the cooldown ends if the user keeps the app open for more than 2 hours. However, the
     * cooldown will reset correctly once the app restarts.
     */
    val checkInPromptAllowed: StateFlow<Boolean> = dataStore.data
        .map { prefs ->
            val currentTime = System.currentTimeMillis()
            val currentDate = LocalDate.now(zone).toString()
            val suppressUntil = prefs[KEY_CHECKIN_SUPPRESS_UNTIL] ?: 0L
            val lastCheckInDate = prefs[KEY_CHECKIN_LAST_DATE]
            val cooldownOver = currentTime >= suppressUntil
            val notCheckedInToday = lastCheckInDate != currentDate
            cooldownOver && notCheckedInToday
        }
        .stateIn(
            CoroutineScope(Dispatchers.Main),
            SharingStarted.Eagerly,
            true
        )


    /**
     * Sets a temporary 2 hour suppression window during which the check-in prompt will not appear.
     * Persists the suppression time using the DataStore.
     */
    fun markCheckInDismissedFor() {
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val until = System.currentTimeMillis() + 2 * 60 * 60 * 1000
                dataStore.edit { it[KEY_CHECKIN_SUPPRESS_UNTIL] = until }
            } catch (e: Exception) {
                Log.e("CheckInRepository", "Failed to mark check-in dismissed", e)
            }
        }
    }

    /**
     * Records that the user has completed a check-in today by storing the current date in the
     * DataStore. Used to prevent additional prompts for the remainder of the day after a check in.
     */
    fun markCheckInToday() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = LocalDate.now(zone).toString()
                dataStore.edit { it[KEY_CHECKIN_LAST_DATE] = today }
            } catch (e: Exception){
                Log.e("CheckInRepository", "Failed to write check-in date", e)
            }
        }
    }

    /**
     * Logs a completed workout to the backend. Returns true if the mutation succeeded, false otherwise.
     */
    suspend fun logWorkoutFromCheckIn(): Boolean {
        val userId = userInfoRepository.getUserIdFromDataStore()?.toIntOrNull() ?: return false
        val time = Instant.now().toString()

        return try {
            val response = apolloClient
                .mutation(LogWorkoutMutation(workoutTime = time, id = userId))
                .execute()

            val ok = response.data?.logWorkout?.workoutFields != null && !response.hasErrors()
            if (!ok) {
                Log.e("CheckInRepository", "LogWorkout errors=${response.errors}")
            }
            ok
        } catch (t: Throwable){
            Log.e("CheckInRepository", "LogWorkout exception", t)
            false
        }
    }
}