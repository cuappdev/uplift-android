package com.cornellappdev.uplift.data.mappers

import com.cornellappdev.uplift.PopularTimesQuery
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTime
import com.cornellappdev.uplift.type.DayOfWeekGraphQLEnum

fun PopularTimesQuery.GetHourlyAverageCapacitiesByFacilityId.toPopularTime(): PopularTime {
    return PopularTime(
        averagePercent = averagePercent,
        hourOfDay = hourOfDay,
        dayOfWeek = dayOfWeek?.let { dayOfWeekToInt(it) } ?: -1
    )
}

private fun dayOfWeekToInt(dayOfWeek: DayOfWeekGraphQLEnum): Int {
    return when (dayOfWeek) {
        DayOfWeekGraphQLEnum.MONDAY -> 1
        DayOfWeekGraphQLEnum.TUESDAY -> 2
        DayOfWeekGraphQLEnum.WEDNESDAY -> 3
        DayOfWeekGraphQLEnum.THURSDAY -> 4
        DayOfWeekGraphQLEnum.FRIDAY -> 5
        DayOfWeekGraphQLEnum.SATURDAY -> 6
        DayOfWeekGraphQLEnum.SUNDAY -> 7
        DayOfWeekGraphQLEnum.UNKNOWN__ -> -1
    }
}