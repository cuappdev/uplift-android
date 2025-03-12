package com.cornellappdev.uplift.data.mappers

import com.cornellappdev.uplift.PopularTimesQuery
import com.cornellappdev.uplift.data.adapters.toInt
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTime

fun PopularTimesQuery.GetHourlyAverageCapacitiesByFacilityId.toPopularTime(): PopularTime {
    return PopularTime(
        averagePercent = averagePercent * 100,
        hourOfDay = hourOfDay,
        dayOfWeek = dayOfWeek?.toInt() ?: -1
    )
}