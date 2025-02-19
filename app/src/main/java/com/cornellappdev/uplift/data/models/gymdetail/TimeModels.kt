package com.cornellappdev.uplift.data.models.gymdetail

import java.util.Calendar

// TODO: Justin is dumb and didn't realize Time models exist in the form of stuff like [Calendar]
//  built into Kotlin/Java. Since my implementations may be buggy, it's probably best to replace any
//  instances of these classes with Calendar (or some other Kotlin library) instead.
//  Note: I still think a distinct TimeInterval class is good, but you can probably implement this
//  with Calendars instead.

/** A [TimeInterval] is one contiguous interval of time with a start and end [TimeOfDay]. */
data class TimeInterval(
    val start: TimeOfDay,
    val end: TimeOfDay
) {
    override fun toString(): String {
        return "$start - $end"
    }

    override fun equals(other: Any?): Boolean {
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

    /**
     * Returns true if [time] is contained within this interval. False otherwise.
     */
    fun within(time: TimeOfDay): Boolean {
        return time.compareTo(start) >= 0 && time.compareTo(end) <= 0
    }

    /**
     * Returns the number of minutes that this interval lasts for, assuming that [end]
     * comes strictly after [start].
     */
    fun durationMinutes(): Int {
        return ((end.hour + (if (end.isAM) 0 else 12) + (if (end.compareTo(start) < 0) 24 else 0)) * 60 + end.minute) -
                ((start.hour + if (start.isAM) 0 else 12) * 60 + start.minute)
    }
}

/**
 * A [TimeOfDay] represents a time of day down to the hour and minute. Can also be specified to be
 * AM or PM.
 * */
data class TimeOfDay(
    /** An hour between 0 and 11, inclusive. An hour of 0 corresponds to 12 in actual time. */
    var hour: Int,
    /** A minute between 0 and 59, inclusive. */
    var minute: Int = 0,
    var isAM: Boolean = true
) {
    init {
        // Coerces [hour] and [minute] to fit according to above invariants.
        val newHour = (hour + (minute) / 60) % 12
        val overlaps = (hour + (minute) / 60) / 12

        this.hour = newHour
        this.minute = (minute) % 60
        this.isAM = if ((overlaps % 2 == 0)) isAM else !isAM
    }

    /**
     * Returns a new [TimeOfDay] created by advancing the current time of day by [deltaHours] and [deltaMinutes].
     */
    fun getTimeLater(deltaMinutes: Int, deltaHours: Int): TimeOfDay {
        return TimeOfDay(
            hour + deltaHours,
            minute + deltaMinutes,
            isAM
        )
    }

    override fun toString(): String {
        return "${if (hour == 0) 12 else hour}:${if (minute.toString().length == 1) "0$minute" else "$minute"} ${if (isAM) "AM" else "PM"}"
    }

    override fun equals(other: Any?): Boolean {
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

    /**
     * Returns a negative integer if this time comes before [other], 0 if they are the same time,
     * and a positive integer if this time comes after [other].
     */
    fun compareTo(other: TimeOfDay): Int {
        if (other.isAM && !isAM) return 1
        if (!other.isAM && isAM) return -1

        if (other.hour != hour) {
            return (hour) - (other.hour)
        }

        return minute - other.minute
    }

    /**
     * Returns the time of this [TimeOfDay] as the number of milliseconds from 12AM.
     *
     * @param c A Calendar which specifies the year, month, and day of this time.
     */
    fun timeInMillis(c: Calendar): Long {
        val newC = c.clone() as Calendar
        newC.set(Calendar.AM_PM, if (isAM) Calendar.AM else Calendar.PM)
        newC.set(Calendar.HOUR, hour)
        newC.set(Calendar.MINUTE, minute)

        return newC.timeInMillis
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

/**
 * Representation type of a facility's open or closed status.
 */
enum class OpenType {
    NOT_APPLICABLE, OPEN, CLOSED
}
