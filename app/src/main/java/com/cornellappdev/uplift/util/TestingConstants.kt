package com.cornellappdev.uplift.util

import androidx.compose.runtime.mutableStateListOf
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.*
import java.util.*

val exCalendarMarch25: Calendar = GregorianCalendar(2023, 2, 25)

val exampleClassMusclePump6 = UpliftClass(
    name = "Muscle Pump",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(11, 15, isAM = true), TimeOfDay(0, 45, isAM = false)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = listOf(),
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump5 = UpliftClass(
    name = "Muscle Pump",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 30, isAM = false), TimeOfDay(11, 15, isAM = false)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = listOf(),
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump4 = UpliftClass(
    name = "Muscle Pump",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 15, isAM = false), TimeOfDay(0, 30, isAM = true)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = listOf(),
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump3 = UpliftClass(
    name = "Muscle Pump",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 30, isAM = false), TimeOfDay(0, 15, isAM = true)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = listOf(),
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump2 = UpliftClass(
    name = "Muscle Pump",
    location = "Teagle Multipurpose Room",
    instructorName = "Claire M.",
    date = exCalendarMarch25,
    time = TimeInterval(TimeOfDay(10, 30, isAM = true), TimeOfDay(11, 15, isAM = true)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = listOf(),
    imageUrl = "https://content.active.com/Assets/Active.com+Content+Site+Digital+Assets/Fitness/Best+Rowing+Apps/rowing-apps_header.jpg"
)

val exampleClassMusclePump1 = UpliftClass(
    name = "Muscle Pump",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 30, isAM = true), TimeOfDay(0, 15, isAM = false)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = listOf(exampleClassMusclePump2),
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
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

val testMorrison = UpliftGym(
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
    popularTimes = List(7) {
        PopularTimes(
            startTime = TimeOfDay(8, 0, true),
            busyList = listOf(50, 30, 3, 60, 90, 0, 20, 11, 6, 93, 76, 0, 8, 9, 100)
        )
    },
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
    classesToday = mutableStateListOf(
        exampleClassMusclePump1,
        exampleClassMusclePump2
    ),
    imageUrl = "https://recreation.athletics.cornell.edu/sites/recreation.athletics.cornell.edu/files/photo-galleries/DB%20_%20Benches_TM.jpeg"
)

val sports: List<Sport> = listOf(
    Sport(painterId = R.drawable.ic_bowling_pins, name = "Bowling"),
    Sport(painterId = R.drawable.ic_dumbbell, name = "Lifting"),
    Sport(painterId = R.drawable.ic_basketball_hoop, name = "Basketball"),
    Sport(painterId = R.drawable.ic_swimming_pool, name = "Swimming"),
).sorted()