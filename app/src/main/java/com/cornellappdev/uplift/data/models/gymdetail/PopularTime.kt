package com.cornellappdev.uplift.data.models.gymdetail

data class PopularTime(
    val averagePercent: Double,
    // 0 is 12:00 AM, 1 is 1:00 AM, ..., 23 is 11:00 PM
    val hourOfDay: Int,
    // 1 is Monday, ..., 7 is Sunday
    val dayOfWeek: Int,
)