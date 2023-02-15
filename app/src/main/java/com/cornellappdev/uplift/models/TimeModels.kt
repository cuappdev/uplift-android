package com.cornellappdev.uplift.models

/** A [TimeInterval] is one interval of time with a start and end [TimeOfDay]. */
data class TimeInterval(
    val start: TimeOfDay,
    val end: TimeOfDay
) {
    override fun toString(): String {
        return "$start - $end"
    }
}

/**
 * A [TimeOfDay] represents a time of day down to the hour and minute. Can also be specified to be
 * AM or PM.
 * */
data class TimeOfDay(
    val hour: Int,
    val minute: Int,
    val isAM: Boolean
) {
    override fun toString(): String {
        return "$hour:${if (minute.toString().length == 1) "0$minute" else "$minute"}${if (isAM) "AM" else "PM"}"
    }
}

/**
 * A [PopularTimes] object represents the busyness of a location over several consecutive hours of
 * a day.
 */
data class PopularTimes(
    val startTime: TimeOfDay,
    /**
     * A list of ints in [0..100] that indicates how busy a gym is at a particular hour.
     * The first int in [busyList] represents the time designated by [startTime]. All other floats
     * represent one hour after the previous int in the list.
     */
    val busyList: List<Int>
)