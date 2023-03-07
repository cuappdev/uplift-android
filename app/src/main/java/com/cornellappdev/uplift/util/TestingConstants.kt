package com.cornellappdev.uplift.util

import com.cornellappdev.uplift.models.*
import java.util.*

val exCalendarMarch25: Calendar = GregorianCalendar(2023,2,25)

val exampleClassMusclePump = UpliftClass(
    name = "Muscle Pump",
    location = "Teagle Multipurpose Room",
    instructorName = "Claire M.",
    minutes = 60,
    date = exCalendarMarch25,
    time = TimeInterval(TimeOfDay(10, 30, isAM = true), TimeOfDay(11, 30, isAM = true)),
    functions = listOf("Idk breh muscles"),
    preparation = "? Get ready to pump",
    description = "Yeah get ready to pump...",
    nextSessions = listOf(),
)

val exampleSwimmingList2 = listOf(
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(7, 0, true),
            end = TimeOfDay(7, 30, true)
        ),
        womenOnly = false
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(8, 0, true),
            end = TimeOfDay(8, 55, true)
        ),
        womenOnly = true
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(10, 0, true),
            end = TimeOfDay(1, 15, false)
        ),
        womenOnly = false
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(4, 0, false),
            end = TimeOfDay(6, 45, false)
        ),
        womenOnly = true
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(8, 30, false),
            end = TimeOfDay(11, 0, false)
        ),
        womenOnly = false
    )
)

val exampleSwimmingList1 = listOf(
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(7, 0, true),
            end = TimeOfDay(7, 45, true)
        ),
        womenOnly = false
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(8, 0, true),
            end = TimeOfDay(8, 45, true)
        ),
        womenOnly = true
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(11, 0, true),
            end = TimeOfDay(1, 30, false)
        ),
        womenOnly = false
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(5, 0, false),
            end = TimeOfDay(6, 30, false)
        ),
        womenOnly = true
    ),
    SwimmingTime(
        time = TimeInterval(
            start = TimeOfDay(8, 30, false),
            end = TimeOfDay(10, 0, false)
        ),
        womenOnly = false
    )
)

val exampleBowlingList = listOf(
    BowlingInfo(
        hours = listOf(
            TimeInterval(
                start = TimeOfDay(hour = 5, minute = 0, isAM = false),
                end = TimeOfDay(hour = 10, minute = 0, isAM = false)
            )
        ),
        pricePerGame = "$3.50",
        shoeRental = "$2.50"
    ),
    BowlingInfo(
        hours = listOf(
            TimeInterval(
                start = TimeOfDay(hour = 3, minute = 0, isAM = false),
                end = TimeOfDay(hour = 7, minute = 0, isAM = false)
            )
        ),
        pricePerGame = "$3.50",
        shoeRental = "$2.50"
    ),
    BowlingInfo(
        hours = listOf(
            TimeInterval(
                start = TimeOfDay(hour = 3, minute = 0, isAM = false),
                end = TimeOfDay(hour = 5, minute = 0, isAM = false)
            ),
            TimeInterval(
                start = TimeOfDay(hour = 5, minute = 30, isAM = false),
                end = TimeOfDay(hour = 10, minute = 0, isAM = false)
            )
        ),
        pricePerGame = "$5.50",
        shoeRental = "$3.50"
    ),
    BowlingInfo(
        hours = listOf(
            TimeInterval(
                start = TimeOfDay(hour = 3, minute = 0, isAM = false),
                end = TimeOfDay(hour = 7, minute = 0, isAM = false)
            )
        ),
        pricePerGame = "$3.50",
        shoeRental = "$2.50"
    ),
    null,
    BowlingInfo(
        hours = listOf(
            TimeInterval(
                start = TimeOfDay(hour = 3, minute = 0, isAM = false),
                end = TimeOfDay(hour = 7, minute = 0, isAM = false)
            )
        ),
        pricePerGame = "$3.50",
        shoeRental = "$2.50"
    ),
    BowlingInfo(
        hours = listOf(
            TimeInterval(
                start = TimeOfDay(hour = 3, minute = 0, isAM = false),
                end = TimeOfDay(hour = 7, minute = 0, isAM = false)
            )
        ),
        pricePerGame = "$3.50",
        shoeRental = "$2.50"
    ),
)

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
            listOf(
                Pair("Precor treadmills", 10),
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
    gymnasiumInfo = listOf(
        GymnasiumInfo(
            hours = listOf(
                TimeInterval(start = TimeOfDay(6), end = TimeOfDay(7)),
                TimeInterval(start = TimeOfDay(7, 30), end = TimeOfDay(10)),
            ),
            courts = listOf(
                CourtInfo(
                    "Basketball", hours = listOf(
                        TimeInterval(start = TimeOfDay(6), end = TimeOfDay(7)),
                        TimeInterval(start = TimeOfDay(7, 30), end = TimeOfDay(10)),
                    )
                ),
                CourtInfo(
                    "Volleyball", hours = listOf(
                        TimeInterval(start = TimeOfDay(7, 30), end = TimeOfDay(10)),
                    )
                )
            )
        ),
        null,
        null,
        null,
        null,
        null,
        null,

        ),
    swimmingInfo = listOf(
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
        null,
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
        SwimmingInfo(swimmingTimes = exampleSwimmingList2),
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
        SwimmingInfo(swimmingTimes = exampleSwimmingList2),
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
    ),
    bowlingInfo = exampleBowlingList,
    miscellaneous = listOf("Game area", "Outdoor basketball court", "Bouldering wall"),
    classesToday = listOf(
        exampleClassMusclePump,
        exampleClassMusclePump
    ),
    imageUrl = "https://recreation.athletics.cornell.edu/sites/recreation.athletics.cornell.edu/files/photo-galleries/DB%20_%20Benches_TM.jpeg"
)