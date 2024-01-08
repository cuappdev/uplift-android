package com.cornellappdev.uplift.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.cornellappdev.uplift.datastoreRepository
import com.cornellappdev.uplift.util.getDistanceBetween
import java.util.Calendar
import kotlin.math.roundToInt

/**
 * A [UpliftGym] object represents all the information needed about one particular gym.
 */
data class UpliftGym(
    val name: String,
    /**
     * The gym id.
     */
    val id: String,
    /**
     * The fitness facility ID (see [id] for gym id).
     */
    val facilityId: String,
    /**
     * A list of exactly 7 lists of time intervals. Each list of time intervals corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday), and the times in said list indicates the hours of
     * this gym. Sorted in ascending order by time.
     *
     * Example: ((7:00 AM - 8:30 AM, 10:00AM - 10:45PM), ...) indicates that on Monday, this
     * gym is open from 7 to 8:30 AM, then closes, and then is open from 10 AM to 10:45 PM.
     * (These are real gym hours for Teagle Down!)
     *
     * If an index of this list is null, that indicates the gym is closed on that day.
     */
    val hours: List<List<TimeInterval>?>,

    /**
     * A list of exactly 7 [PopularTimes] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday).
     */
    val popularTimes: List<PopularTimes>,

    val equipmentGroupings: List<EquipmentGrouping>,

    /** A list of courts at this fitness center. */
    val gymnasiumInfo: List<CourtFacility?>?,

    /**
     * A list of exactly 7 [SwimmingInfo] objects for each day, starting on Monday.
     *
     * If the list itself is null, that indicates swimming is not offered by this gym.
     */
    val swimmingInfo: List<SwimmingInfo?>?,

    /**
     * A list of exactly 7 [SwimmingInfo] objects for each day, starting on Monday.
     *
     * If the list itself is null, that indicates bowling is not offered by this gym.
     */
    val bowlingInfo: List<BowlingInfo?>?,
    val miscellaneous: List<String>,
    val imageUrl: String,
    var classesToday: SnapshotStateList<UpliftClass> = mutableStateListOf(),
    val upliftCapacity: UpliftCapacity?,
    val latitude: Double,
    val longitude: Double
) {
    /**
     * Returns a boolean indicating whether this gym is favorited or not. Safe for recomposition.
     */
    @Composable
    fun isFavorite(): Boolean {
        return datastoreRepository.favoriteGymsFlow.collectAsState().value.contains(facilityId)
    }

    /**
     * Toggles the favorite status of this gym.
     */
    fun toggleFavorite() {
        datastoreRepository.saveFavoriteGym(
            facilityId,
            !datastoreRepository.favoriteGymsFlow.value.contains(facilityId)
        )
    }

    /**
     * Returns the distance from this gym to the user's current location. Safe for recomposition.
     * Returns null if the user's location is not yet initialized.
     */
    fun getDistance(): Float? {
        return LocationRepository.currentLocation.value?.let { user ->
            if (LocationRepository.currentLocation.value != null) {
                getDistanceBetween(latitude, longitude, user.latitude, user.longitude)
            } else {
                null
            }
        }
    }
}

/**
 * A gym's capacity.
 */
data class UpliftCapacity(
    /** The direct percent read from CFC. */
    val percent: Double,
    val updated: Calendar
) {
    /**
     * Returns a string representation of the percentage capacity.
     */
    fun percentString(): String {
        return "${(percent * 100).roundToInt()}%"
    }

    /**
     * Returns a string representing the time at which this capacity was last updated.
     * Of the form `"Updated: HH:MM AM"`
     */
    fun updatedString(): String {
        val timeOfDay = TimeOfDay(
            hour = updated.get(Calendar.HOUR),
            minute = updated.get(Calendar.MINUTE),
            isAM = updated.get(Calendar.AM_PM) == Calendar.AM
        )
        return "Updated: $timeOfDay"
    }
}
