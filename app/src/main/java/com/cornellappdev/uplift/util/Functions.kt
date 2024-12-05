package com.cornellappdev.uplift.util

import android.location.Location
import com.cornellappdev.uplift.data.models.TimeInterval
import com.cornellappdev.uplift.data.models.TimeOfDay
import com.cornellappdev.uplift.data.models.UpliftClass
import java.util.Calendar

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
fun calendarDayToString(calendar: Calendar): String {
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
 * Takes in a calendar and returns a string representation of its day of week.
 *
 * @param abbreviated If the full name of the day should be returned, or an abbreviation.
 * Example: 3/15/2023 as a calendar would return: "Wednesday"
 */
fun calendarDayOfWeekToString(calendar: Calendar, abbreviated: Boolean = false): String {
    val dayString = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> if (!abbreviated) "Monday" else "M"
        Calendar.TUESDAY -> if (!abbreviated) "Tuesday" else "T"
        Calendar.WEDNESDAY -> if (!abbreviated) "Wednesday" else "W"
        Calendar.THURSDAY -> if (!abbreviated) "Thursday" else "Th"
        Calendar.FRIDAY -> if (!abbreviated) "Friday" else "F"
        Calendar.SATURDAY -> if (!abbreviated) "Saturday" else "S"
        else -> if (!abbreviated) "Sunday" else "Su"
    }

    return dayString
}

/**
 * Returns the current system time as a [TimeOfDay] object.
 */
fun getSystemTime(): TimeOfDay {
    val c: Calendar = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR)
    val minute = c.get(Calendar.MINUTE)
    val isAM = c.get(Calendar.AM_PM) == Calendar.AM
    return TimeOfDay(hour, minute, isAM)
}

/**
 * Returns a boolean corresponding to if the facility whose hours are denoted by [times] are open
 * at [timeOfDay]. If [timeOfDay] is not passed a value, it defaults to the system time
 * retrieved by [getSystemTime].
 */
fun isOpen(times: List<TimeInterval>?, timeOfDay: TimeOfDay = getSystemTime()): Boolean {
    if (times == null) return false

    val activeInterval = times.find { interval -> interval.within(timeOfDay) }
    return activeInterval != null
}

/**
 * Returns a boolean corresponding to if this [Calendar] is on the same day as [other].
 */
fun Calendar.sameDayAs(other: Calendar): Boolean =
    get(Calendar.YEAR) == other.get(Calendar.YEAR)
            && get(Calendar.MONTH) == other.get(Calendar.MONTH)
            && get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH)

/**
 * Returns an int corresponding to the day of the week that it is.
 *
 * Mon -> 0
 *
 * Tues -> 1
 *
 * ...
 *
 * Sun -> 6
 */
fun todayIndex(): Int {
    return ((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) + 7) % 7
}

/**
 * Returns this Calendar's hour and minute representation as a [TimeOfDay] object.
 */
fun Calendar.asTimeOfDay(): TimeOfDay =
    TimeOfDay(
        hour = get(Calendar.HOUR),
        minute = get(Calendar.MINUTE),
        isAM = get(Calendar.AM_PM) == Calendar.AM
    )

/**
 * Gets the spatial distance in miles between the two specified points in latitude and longitude.
 */
fun getDistanceBetween(
    latitude1: Double,
    longitude1: Double,
    latitude2: Double,
    longitude2: Double
): Float {
    var results = floatArrayOf(0f)
    Location.distanceBetween(
        latitude1,
        longitude1,
        latitude2,
        longitude2,
        results
    )

    // Currently in meters. Convert to miles.
    results = results.map { meter ->
        meter * 0.000621371f
    }.toFloatArray()


    return if (results.isNotEmpty()) results[0] else -1f
}

/**
 * A comparator for [UpliftClass]es that will sort in ascending order by start time,
 * irrespective of day.
 */
val startTimeComparator = { class1: UpliftClass, class2: UpliftClass ->
    if (class1.time.start.compareTo(class2.time.start) != 0) {
        class1.time.start.compareTo(class2.time.start)
    } else {
        class1.time.end.compareTo(class2.time.end)
    }
}
