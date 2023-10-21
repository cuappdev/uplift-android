package com.cornellappdev.uplift.util

import androidx.compose.runtime.mutableStateListOf
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.BowlingInfo
import com.cornellappdev.uplift.models.Capacity
import com.cornellappdev.uplift.models.CourtInfo
import com.cornellappdev.uplift.models.EquipmentGrouping
import com.cornellappdev.uplift.models.Gear
import com.cornellappdev.uplift.models.GymnasiumInfo
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.models.Sport
import com.cornellappdev.uplift.models.SwimmingInfo
import com.cornellappdev.uplift.models.SwimmingTime
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftActivity
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import java.util.Calendar
import java.util.GregorianCalendar


val sampleLocation_A = UpliftActivity(
    name = "Sample Location_A",
    id = "location_id_1",
    starred = false,
    gymId = "gym_id_1",
    address = "123 Sample Street",
    hours = listOf(
        listOf(
            TimeInterval(TimeOfDay(8, 0, true), TimeOfDay(12, 0, true)),
            TimeInterval(TimeOfDay(13, 0, true), TimeOfDay(17, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(11, 30, true)),
            TimeInterval(TimeOfDay(14, 0, true), TimeOfDay(18, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(12, 30, true)),
            TimeInterval(TimeOfDay(15, 0, true), TimeOfDay(19, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(8, 30, true), TimeOfDay(11, 0, true)),
            TimeInterval(TimeOfDay(13, 30, true), TimeOfDay(17, 30, true))
        ),
        null, // Location closed on Thursday
        listOf(
            TimeInterval(TimeOfDay(10, 0, true), TimeOfDay(13, 0, true)),
            TimeInterval(TimeOfDay(16, 0, true), TimeOfDay(20, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(12, 0, true)),
            TimeInterval(TimeOfDay(14, 0, true), TimeOfDay(18, 0, true))
        )
    ),
    pricing = listOf(
        Pair("one-time", 10),  // One-time visit pricing
        Pair("membership", 60) // Monthly membership pricing
    ),
    equipment = listOf(Pair("Shoes", 10), Pair("Racket", 1)),
    services = listOf("showers", "lockers", "parking"),
    gear = setOf(Gear.NOT_REQUIRED, Gear.BRING_YOURSELF, Gear.CAN_RENT),
    reservation = true,
    imageUrl = "sample_image_url"
)

val sampleLocation_B = UpliftActivity(
    name = "Sample Location_B",
    id = "location_id_2",
    starred = false,
    gymId = "gym_id_2",
    address = "456 Sample Street",
    hours = listOf(
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(11, 30, true)),
            TimeInterval(TimeOfDay(14, 0, true), TimeOfDay(18, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(11, 30, true)),
            TimeInterval(TimeOfDay(14, 0, true), TimeOfDay(18, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(11, 30, true)),
            TimeInterval(TimeOfDay(14, 0, true), TimeOfDay(18, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(8, 30, true), TimeOfDay(11, 0, true)),
            TimeInterval(TimeOfDay(13, 30, true), TimeOfDay(17, 30, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(8, 30, true), TimeOfDay(11, 0, true)),
            TimeInterval(TimeOfDay(13, 30, true), TimeOfDay(17, 30, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(10, 0, true), TimeOfDay(13, 0, true)),
            TimeInterval(TimeOfDay(16, 0, true), TimeOfDay(20, 0, true))
        ),
        listOf(
            TimeInterval(TimeOfDay(9, 0, true), TimeOfDay(12, 0, true)),
            TimeInterval(TimeOfDay(14, 0, true), TimeOfDay(18, 0, true))
        )
    ),
    pricing = null,
    equipment = listOf(Pair("gear sample", 2)),
    services = listOf("showers", "lockers", "parking"),
    gear = setOf(Gear.NOT_REQUIRED),
    reservation = true,
    imageUrl = "sample_image_url"
)


val exCalendarMarch25: Calendar = GregorianCalendar(2023, 2, 25)

val exampleClassMusclePump6 = UpliftClass(
    id = "0",
    name = "Muscle Pump", gymId = "",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(11, 15, isAM = true), TimeOfDay(0, 45, isAM = false)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump5 = UpliftClass(
    id = "0",
    name = "Muscle Pump", gymId = "",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 30, isAM = false), TimeOfDay(11, 15, isAM = false)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump4 = UpliftClass(
    id = "0",
    name = "Muscle Pump", gymId = "",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 15, isAM = false), TimeOfDay(0, 30, isAM = true)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump3 = UpliftClass(
    id = "0",
    name = "Muscle Pump", gymId = "",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 2, 24),
    time = TimeInterval(TimeOfDay(0, 30, isAM = false), TimeOfDay(0, 15, isAM = true)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    imageUrl = "https://post.healthline.com/wp-content/uploads/2020/01/Young-Women-Having-Sports-Rowing-Training-Drill-in-Gym-732x549-thumbnail.jpg"
)

val exampleClassMusclePump2 = UpliftClass(
    id = "0",
    name = "Muscle Pump",
    gymId = "",
    location = "Teagle Multipurpose Room",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 8, 7),
    time = TimeInterval(TimeOfDay(10, 30, isAM = true), TimeOfDay(11, 15, isAM = true)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    imageUrl = "https://www.mensjournal.com/.image/t_share/MTk2MTM3MzI0NDcwMzQ3MjY5/rowing-1.jpg"
)

val exampleClassMusclePump1 = UpliftClass(
    id = "0",
    name = "Muscle Pump", gymId = "",
    location = "Helen Newman Hall",
    instructorName = "Claire M.",
    date = GregorianCalendar(2023, 8, 6),
    time = TimeInterval(TimeOfDay(0, 30, isAM = true), TimeOfDay(0, 15, isAM = false)),
    functions = listOf("Core", "Overall Fitness", "Stability"),
    preparation = "Footwear appropriate for movement",
    description = "Put a little muscle into your workout and join us for a class designed to build muscle endurance with low to medium weights and high repetitions. A variety of equipment and strength training techniques will be used in this class. There is no cardio portion in these sessions. Footwear that is appropriate for movement is required for this class. ",
    nextSessions = mutableStateListOf(exampleClassMusclePump2),
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
    id = "Super id-y id",
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
        null
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
    imageUrl = "https://recreation.athletics.cornell.edu/sites/recreation.athletics.cornell.edu/files/photo-galleries/DB%20_%20Benches_TM.jpeg",
    capacity = Capacity(Pair((Math.random() * 20 + 100).toInt(), 140), Calendar.getInstance())
)

val sports: List<Sport> = listOf(
    Sport(painterId = R.drawable.ic_bowling_pins, name = "Bowling"),
    Sport(painterId = R.drawable.ic_dumbbell, name = "Lifting"),
    Sport(painterId = R.drawable.ic_basketball_hoop, name = "Basketball"),
    Sport(painterId = R.drawable.ic_swimming_pool, name = "Swimming"),
).sorted()
