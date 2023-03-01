package com.cornellappdev.uplift.util

import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay

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