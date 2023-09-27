package com.cornellappdev.uplift.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.cornellappdev.uplift.datastoreRepository
import java.util.Calendar
import kotlin.math.roundToInt

/**
 * A [UpliftGym] object represents all the information needed about one particular gym.
 */
data class UpliftGym(
    val name: String,
    val id: String,
    /**
     * A list of exactly 7 lists of time intervals. Each list of time intervals corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday), and the times in said list indicates the hours of
     * this gym.
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
    /**
     * A list of exactly 7 [GymnasiumInfo] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday). A null object indicates that the gymnasium is closed
     * that day.
     *
     * If the list itself is null, that indicates gymnasiums are not offered by this gym.
     */
    val gymnasiumInfo: List<GymnasiumInfo?>?,

    /**
     * A list of exactly 7 [SwimmingInfo] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday). A null object indicates that the pool is closed
     * that day.
     *
     * If the list itself is null, that indicates swimming is not offered by this gym.
     */
    val swimmingInfo: List<SwimmingInfo?>?,

    /**
     * A list of exactly 7 [SwimmingInfo] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday). A null object indicates that the pool is closed
     * that day.
     *
     * If the list itself is null, that indicates bowling is not offered by this gym.
     */
    val bowlingInfo: List<BowlingInfo?>?,
    val miscellaneous: List<String>,
    val imageUrl: String,
    var classesToday: SnapshotStateList<UpliftClass> = mutableStateListOf(),
    val upliftCapacity: UpliftCapacity?
) {
    /**
     * Returns a boolean indicating whether this gym is favorited or not. Safe for recomposition.
     */
    @Composable
    fun isFavorite(): Boolean {
        return datastoreRepository.favoriteGymsFlow.collectAsState().value.contains(id)
    }

    /**
     * Toggles the favorite status of this gym.
     */
    fun toggleFavorite() {
        datastoreRepository.saveFavoriteGym(
            id,
            !datastoreRepository.favoriteGymsFlow.value.contains(id)
        )
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
