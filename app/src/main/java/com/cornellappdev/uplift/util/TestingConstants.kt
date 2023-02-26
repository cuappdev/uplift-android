package com.cornellappdev.uplift.util

import com.cornellappdev.uplift.models.*

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
                TimeOfDay(hour = 4, minute = 0, isAM = false),
            ),
            TimeInterval(
                TimeOfDay(hour = 6, minute = 0, isAM = false),
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
                TimeOfDay(hour = 2, minute = 0, isAM = true),
                TimeOfDay(hour = 4, minute = 0, isAM = true),
            ),
            TimeInterval(
                TimeOfDay(hour = 6, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
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
    equipmentGroupings = listOf(
        EquipmentGrouping(
            "Cardio Machines",
            listOf(Pair("Precor treadmills", 10),
                Pair("Elliptical trainers", 12),
                Pair("AMTs", 4),
                Pair("Expresso bikes", 5),
                Pair("Recumbent and upright bikes", 3),
            )
        ),
        EquipmentGrouping(
            "Precor Selectorized Machines",
            listOf(
                Pair("Leg Press", 10),
                Pair("Seated Calf Raises", 12),
                Pair("Seated Leg Curl", 4),
                Pair("Leg Extensions", 5),
                Pair("Inner/Outer ttwtf is this", 3),
                Pair("Glute Extensions", 3),
                Pair("Converging Sus Machine", 3),
                Pair("Converging Chin Definer", 3),
                Pair("Dip/Chin Assistance", 3),
                Pair("Rear Deltoid Pulldowns", 3),
            )
        )
    ),
    gymnasiumInfo = listOf(),
    swimmingInfo = listOf(),
    bowlingInfo = listOf(),
    miscellaneous = listOf(),
    classesToday = listOf(),
    imageUrl = "https://recreation.athletics.cornell.edu/sites/recreation.athletics.cornell.edu/files/photo-galleries/DB%20_%20Benches_TM.jpeg"
)