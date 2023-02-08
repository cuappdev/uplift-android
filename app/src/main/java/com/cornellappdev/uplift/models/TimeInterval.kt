package com.cornellappdev.uplift.models

data class TimeInterval(
    val start: TimeOfDay,
    val end: TimeOfDay
) {
    override fun toString(): String {
        return "$start - $end"
    }
}

data class TimeOfDay(
    val hour: Int,
    val minute: Int,
    val isAM: Boolean
) {
    override fun toString(): String {
        return "$hour:${if (minute.toString().length == 1) "0$minute" else "$minute"}${if (isAM) "AM" else "PM"}"
    }
}

data class PopularTimes(
    val startTime: TimeOfDay,
    /**
     * A list of floats in [0..100] that indicates how busy a gym is at a particular hour.
     */
    val busyList : List<Float>
)