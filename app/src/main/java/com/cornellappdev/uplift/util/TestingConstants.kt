package com.cornellappdev.uplift.util

import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay

val testMorrison = Gym(
    name = "Toni Morrison Gym",
    hours = listOf(
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 12, minute = 0, isAM = false),
                TimeOfDay(hour = 10, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 12, minute = 0, isAM = false),
                TimeOfDay(hour = 10, minute = 0, isAM = false),
            )
        ),
    ),
    popularTimes = PopularTimes(
        startTime = TimeOfDay(8, 0, true),
        busyList = listOf(50, 30, 3, 60, 90, 0, 20, 11, 6, 93, 76, 0, 8, 9, 100)
    ),
    equipmentGroupings = listOf(),
    gymnasiumInfo = listOf(),
    swimmingInfo = listOf(),
    bowlingInfo = listOf(),
    miscellaneous = listOf(),
    classesToday = listOf(),
    imageUrl = "https://recreation.athletics.cornell.edu/sites/recreation.athletics.cornell.edu/files/photo-galleries/DB%20_%20Benches_TM.jpeg"
)