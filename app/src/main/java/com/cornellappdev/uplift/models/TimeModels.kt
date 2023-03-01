package com.cornellappdev.uplift.models

/** A [TimeInterval] is one interval of time with a start and end [TimeOfDay]. */
data class TimeInterval(
    val start: TimeOfDay,
    val end: TimeOfDay
) {
    override fun toString(): String {
        return "$start - $end"
    }

    override fun equals(other : Any?) : Boolean {
        if (other !is TimeInterval) {
            return false
        }

        return start == other.start && end == other.end
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }
}

/**
 * A [TimeOfDay] represents a time of day down to the hour and minute. Can also be specified to be
 * AM or PM.
 * */
data class TimeOfDay(
    val hour: Int,
    val minute: Int = 0,
    val isAM: Boolean = true
) {
    fun getTimeLater(deltaMinutes: Int, deltaHours: Int): TimeOfDay {
        var newHour = (hour + deltaHours + (minute + deltaMinutes) / 60) % 12
        val overlaps = (hour + deltaHours + (minute + deltaMinutes) / 60) / 12
        if (newHour == 0) newHour = 12

        return TimeOfDay(
            newHour,
            (minute + deltaMinutes) % 60,
            if ((overlaps % 2 == 0)) isAM else !isAM
        )
    }

    override fun toString(): String {
        return "$hour:${if (minute.toString().length == 1) "0$minute" else "$minute"}${if (isAM) "AM" else "PM"}"
    }

    override fun equals(other : Any?) : Boolean {
        if (other !is TimeOfDay) {
            return false
        }

        return other.hour == hour && other.minute == minute && other.isAM == isAM
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minute
        result = 31 * result + isAM.hashCode()
        return result
    }

    fun compareTo(other : TimeOfDay) : Int {
        if (other.isAM && !isAM) return 1
        if (!other.isAM && isAM) return -1

        if (other.hour != hour) {
            return hour - other.hour
        }

        return minute - other.minute
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