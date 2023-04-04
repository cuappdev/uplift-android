package com.cornellappdev.uplift.networking

import com.cornellappdev.uplift.models.*
import com.cornellappdev.uplift.util.defaultGymUrl
import com.cornellappdev.uplift.util.parseTimeOfDay
import com.example.rocketreserver.GymListQuery

fun parsePopularTimes(times: List<List<Int?>?>?): List<PopularTimes> {
    if (times == null) return listOf()

    // Making the assumption that these are indexed with 0=Sunday on the backend.
    val timesMondayZero = List(7) {
        when (it) {
            6 -> times[0]
            else -> times[it + 1]
        }
    }

    val popularTimesList: List<PopularTimes> = timesMondayZero.map { timesThroughDay ->
        if (timesThroughDay == null) PopularTimes(TimeOfDay(12), listOf(0))
        else {
            val firstNonZero = timesThroughDay.indexOfFirst { num -> num != 0 }
            val lastNonZero = timesThroughDay.indexOfLast { num -> num != 0 }

            val popularTimes = PopularTimes(
                startTime = TimeOfDay(12).getTimeLater(deltaMinutes = 0, deltaHours = firstNonZero),
                busyList = timesThroughDay.subList(firstNonZero, lastNonZero + 1)
                    .map { numNullable -> numNullable ?: 0 }
            )

            popularTimes
        }
    }

    return popularTimesList
}

fun parseHours(times: List<GymListQuery.Time1?>): List<List<TimeInterval>?> {
    val timesMondayZero = List(7) {
        times.find { time ->
            // Corrects days to start with Monday = 0.
            val dayCorrected = when (time?.day) {
                0 -> 6
                else -> time?.day?.minus(1)
            }

            dayCorrected == it
        }
    }

    val timeIntervalList = timesMondayZero.map { time ->
        time?.let { timeNonNull ->
            val start = parseTimeOfDay(timeNonNull.startTime.toString())
            val end = parseTimeOfDay(timeNonNull.endTime.toString())

            if (start == end) null
            else
                // It seems the backend request structure is only made for one contiguous time
                // interval... so I guess for now just make a list of one time interval, only, ever...?
                listOf(TimeInterval(start, end))
        }
    }

    return timeIntervalList
}

fun pullEquipmentGroupings(gym: GymListQuery.Gym): List<EquipmentGrouping> {
    val equipmentFacility = gym.facilities.find { facility ->
        facility?.name == "Fitness Center"
    } ?: return listOf()

    val equipmentDetail = equipmentFacility.details.find { detail ->
        detail?.detailsType == "Equipment"
    } ?: return listOf()

    val groups: MutableMap<String, MutableList<Pair<String, Int>>> = mutableMapOf()
    equipmentDetail.equipment.filterNotNull().forEach { equipment ->
        if (!groups.containsKey(equipment.equipmentType)) {
            groups[equipment.equipmentType] = mutableListOf()
        }
        // Post-condition: groups[equipment.equipmentType] is a list without the current equipment.

        groups[equipment.equipmentType]?.add(
            Pair(
                equipment.name,
                try {
                    equipment.quantity.toInt()
                } catch (_: java.lang.NumberFormatException) {
                    1
                }
            )
        )
    }

    return groups.map { (key, mutableList) ->
        EquipmentGrouping(name = key, equipmentList = mutableList.toList())
    }
}

fun pullMiscellaneous(gym: GymListQuery.Gym): List<String> {
    val miscFacility = gym.facilities.find { facility ->
        facility?.name == "Miscellaneous"
    } ?: return listOf()

    val subFacilitiesDetail = miscFacility.details.find { detail ->
        detail?.detailsType == "Sub-Facilities"
    } ?: return listOf()

    return subFacilitiesDetail.subFacilityNames.filterNotNull()
}

fun pullBowling(gym: GymListQuery.Gym): List<BowlingInfo?>? {
    val bowlingFacility = gym.facilities.find { facility ->
        facility?.name == "Bowling Alley"
    } ?: return null

    val hoursDetail = bowlingFacility.details.find { detail ->
        detail?.detailsType == "Hours"
    }

    val pricesDetail = bowlingFacility.details.find { detail ->
        detail?.detailsType == "Prices"
    }

    val pricesList = pricesDetail?.prices

    val pricePerGame = if (pricesList == null || pricesList.isEmpty()) "-" else pricesList[0]
    val shoeRental = if (pricesList == null || pricesList.size < 2) "-" else pricesList[1]

    val hoursMondayZero = List(7) {
        hoursDetail?.times?.find { time ->
            // Corrects days to start with Monday = 0.
            val dayCorrected = when (time?.day) {
                0 -> 6
                else -> time?.day?.minus(1)
            }

            dayCorrected == it
        }
    }

    val bowlingInfos = hoursMondayZero.map { time ->
        val timeIntervals = time?.timeRanges?.map { range ->
            TimeInterval(
                parseTimeOfDay(range?.startTime.toString()),
                parseTimeOfDay(range?.endTime.toString())
            )
        }

        if (timeIntervals != null && pricePerGame != null && shoeRental != null) {
            BowlingInfo(timeIntervals, pricePerGame, shoeRental)
        } else null
    }

    return bowlingInfos
}

fun pullGymnasiumInfos(gym: GymListQuery.Gym): List<GymnasiumInfo?>? {
    val gymnasiumFacility = gym.facilities.find { facility ->
        facility?.name == "Gymnasium"
    } ?: return null

    val gymnasiumHourDetail = gymnasiumFacility.details.find { detail ->
        detail?.detailsType == "Hours"
    }

    val gymnasiumCourtDetail = gymnasiumFacility.details.find { detail ->
        detail?.detailsType == "Court"
    }

    val gymnasiumHoursMondayZero = List(7) {
        gymnasiumHourDetail?.times?.find { time ->
            // Corrects days to start with Monday = 0.
            val dayCorrected = when (time?.day) {
                0 -> 6
                else -> time?.day?.minus(1)
            }

            dayCorrected == it
        }
    }

    val gymnasiumHours = gymnasiumHoursMondayZero.map {
        it?.timeRanges?.map { range ->
            TimeInterval(
                parseTimeOfDay(range?.startTime.toString()),
                parseTimeOfDay(range?.endTime.toString())
            )
        } ?: listOf()
    }

    val courtHours = List(7) {
        gymnasiumCourtDetail?.times?.find { time ->
            // Corrects days to start with Monday = 0.
            val dayCorrected = when (time?.day) {
                0 -> 6
                else -> time?.day?.minus(1)
            }

            dayCorrected == it
        }
    }

    fun getCourtInfosForDay(day: Int): List<CourtInfo> {
        if (gymnasiumCourtDetail == null) return listOf()

        val numCourts = gymnasiumCourtDetail.subFacilityNames.size / 7

        val list: List<CourtInfo> = List(numCourts) { courtIndex ->
            val subFacilityNameIndex: Int = (day + 1) % 7 + courtIndex * 7

            CourtInfo(
                name = gymnasiumCourtDetail.subFacilityNames[subFacilityNameIndex] ?: "Court",
                hours = courtHours[day]?.timeRanges?.get(courtIndex)?.let { range ->
                    listOf(
                        TimeInterval(
                            parseTimeOfDay(range.startTime.toString()),
                            parseTimeOfDay(range.endTime.toString())
                        )
                    )
                } ?: listOf()
            )
        }

        return list
    }

    return List(7) { day ->
        GymnasiumInfo(gymnasiumHours[day], getCourtInfosForDay(day))
    }
}

fun GymListQuery.Gym.toUpliftGym(): UpliftGym {
    return UpliftGym(
        name = name,
        popularTimes = parsePopularTimes(popular),
        imageUrl = imageUrl ?: defaultGymUrl,
        hours = parseHours(times),
        equipmentGroupings = pullEquipmentGroupings(this),
        miscellaneous = pullMiscellaneous(this),
        bowlingInfo = pullBowling(this),
        // No swimming info in backend yet...
        swimmingInfo = null,
        gymnasiumInfo = pullGymnasiumInfos(this)
    )
}











