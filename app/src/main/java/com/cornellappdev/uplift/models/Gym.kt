package com.cornellappdev.uplift.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

data class Gym(
    val name: String,

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

    val popularTimes : PopularTimes,

    val equipmentGroupings : List<EquipmentGrouping>,

    /**
     * A list of exactly 7 [GymnasiumInfo] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday). A null object indicates that the gymnasium is closed
     * that day.
     */
    val gymnasiumInfo : List<GymnasiumInfo?>,

    /**
     * A list of exactly 7 [SwimmingInfo] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday). A null object indicates that the pool is closed
     * that day.
     */
    val swimmingInfo : List<SwimmingInfo?>,

    /**
     * A list of exactly 7 [SwimmingInfo] objects. Each object corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday). A null object indicates that the pool is closed
     * that day.
     */
    val bowlingInfo : List<BowlingInfo?>,

    val miscellaneous : List<String>,

    val classesToday : List<UpliftClass>,

    val favoriteState : State<Boolean> = mutableStateOf(false)
)

