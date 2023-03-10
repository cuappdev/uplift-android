package com.cornellappdev.uplift.util

import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay
import java.util.*

/**
 * Returns a flavor message corresponding to the numeric wait time input.
 * Requires: [waitTime] is between [0..100].
 */
fun waitTimeFlavorText(waitTime: Int): String {
    if (waitTime < 25) return "Usually not busy"
    else if (waitTime < 50) return "Usually not too busy"
    else if (waitTime < 75) return "Usually pretty busy"
    return "Usually very busy"
}

/**
 * Returns a time interval whose start time is the earliest start time in [times] and whose end time
 * is the latest end time in [times].
 *
 * Requires: [times] is not empty.
 */
fun getOverallTimeInterval(times: List<TimeInterval>): TimeInterval {
    if (times.isEmpty()) {
        return TimeInterval(TimeOfDay(0, 0, true), TimeOfDay(11, 59, false))
    }
    var earliest = times[0].start
    var latest = times[0].end

    for (time in times) {
        if (time.start.compareTo(earliest) < 0) {
            earliest = time.start
        }
        if (time.end.compareTo(latest) > 0) {
            latest = time.end
        }
    }

    return TimeInterval(earliest, latest)
}

/**
 * Takes in a calendar and returns a string representation of the calendar's month and day.
 *
 * Example: Valentine's Day as a calendar would be returned as: "Feb 14"
 */
fun calendarDayToString(calendar : Calendar) : String {
    val month = when (calendar.get(Calendar.MONTH)) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        else -> "Dec"
    }

    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return "$month $day"
}

/**
 * Returns the current system time as a [TimeOfDay] object.
 */
fun getSystemTime() : TimeOfDay {
    val c : Calendar = Calendar.getInstance()
    val hour = if (c.get(Calendar.HOUR) == 0) 12 else c.get(Calendar.HOUR)
    val minute = c.get(Calendar.MINUTE)
    val isAM = c.get(Calendar.AM_PM) == Calendar.AM
    return TimeOfDay(hour, minute, isAM)
}

/**
 * Returns a boolean corresponding to if the facility whose hours are denoted by [times] are open
 * at [timeOfDay]. If [timeOfDay] is not passed a value, it defaults to the system time
 * retrieved by [getSystemTime].
 */
fun isCurrentlyOpen(times : List<TimeInterval>, timeOfDay : TimeOfDay = getSystemTime()) : Boolean {
    return times.find { interval -> interval.within(timeOfDay) } != null
}