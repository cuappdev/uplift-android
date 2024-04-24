package com.cornellappdev.uplift.networking


import com.cornellappdev.uplift.GymListQuery
import com.cornellappdev.uplift.fragment.GymFields
import com.cornellappdev.uplift.fragment.OpenHoursFields
import com.cornellappdev.uplift.models.BowlingInfo
import com.cornellappdev.uplift.models.CourtFacility
import com.cornellappdev.uplift.models.CourtTime
import com.cornellappdev.uplift.models.EquipmentField
import com.cornellappdev.uplift.models.EquipmentGrouping
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.models.SwimmingInfo
import com.cornellappdev.uplift.models.SwimmingTime
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftCapacity
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.type.EquipmentType
import com.cornellappdev.uplift.util.defaultGymUrl
import java.util.Calendar

/**
 * Returns the popular times list representation for this gym query.
 */
fun GymListQuery.GetAllGym.pullPopularTimes(
    facilityIn: GymFields.Facility?
): List<PopularTimes> {
    // TODO: Change to pull actual popular times info when backend adds that.
    return listOf()
}

/**
 * Returns the swimming info for this gym query.
 */
fun GymFields.Facility?.pullSwimmingInfo(): List<SwimmingInfo?>? {
    // If swimming facility doesn't exist, return.
    val hours = this?.facilityFields?.hours?.map {
        it?.openHoursFields
    } ?: return null

    val intervals = pullHours(hours, HourAdditionalData.WOMEN_ONLY).toSwimmingTime()

    return intervals.map { list ->
        if (list != null)
            SwimmingInfo(list)
        else null
    }
}

/** Returns the court info for this fitness center. */
fun GymListQuery.GetAllGym.pullGymnasiumInfo(): List<CourtFacility> {
    val courts = gymFields.facilities?.filterNotNull()?.filter { facility ->
        facility.facilityFields.facilityType.toString() == "COURT"
    } ?: listOf()

    return courts.map { facility ->
        val hours =
            pullHours(
                facility.facilityFields.hours?.map { it?.openHoursFields },
                HourAdditionalData.COURT_TYPE
            ).toCourtTime()

        CourtFacility(facility.facilityFields.name, hours)
    }
}


/**
 * Returns the miscellaneous details for this gym query.
 */
fun GymListQuery.GetAllGym.pullMiscellaneous(): List<String> {
    // TODO: Change to pull actual miscellaneous info when backend adds that.
    return listOf()
}

/**
 * Returns the bowling info for the given bowling facility.
 */
fun GymFields.Facility?.pullBowling(): List<BowlingInfo?>? {
    // If bowling facility doesn't exist, return.
    val hours = this?.facilityFields?.hours?.map {
        it?.openHoursFields
    } ?: return null

    val intervals = pullHours(hours).toTimeInterval()

    return intervals.map {
        if (it != null)
            BowlingInfo(it, "$4.00", "$3.00")
        else null
    }
}


enum class HourAdditionalData {
    NONE, WOMEN_ONLY, COURT_TYPE
}

/**
 * Parses the output of [pullHours] for a NONE input.
 */
fun List<List<Pair<TimeInterval, String>>?>.toTimeInterval(): List<List<TimeInterval>?> {
    return this.map { it?.map { it.first } }
}

/**
 * Parses the output of [pullHours] for a WOMEN_ONLY input.
 */
fun List<List<Pair<TimeInterval, String>>?>.toSwimmingTime(): List<List<SwimmingTime>?> {
    return this.map { it?.map { SwimmingTime(it.first, it.second == "true") } }
}

/**
 * Parses the output of [pullHours] for a COURT_TYPE input.
 */
fun List<List<Pair<TimeInterval, String>>?>.toCourtTime(): List<List<CourtTime>?> {
    return this.map { it?.map { CourtTime(it.first, it.second) } }
}

/**
 * Returns the hours for the given open hour list. Works for any open hour list.
 *
 * Can package some additional data as the second in the tuple:
 * - If additional data is NONE, packages an empty string.
 * - If additional data is WOMEN_ONLY, packages either "true" or "false".
 * - If additional data is COURT_TYPE, packages the name of the court.
 */
fun pullHours(
    hoursFields: List<OpenHoursFields?>?,
    additionalData: HourAdditionalData = HourAdditionalData.NONE
): List<List<Pair<TimeInterval, String>>?> {
    // Initialize to always closed.
    val hoursList: MutableList<MutableList<Pair<TimeInterval, String>>?> = MutableList(7) { null }

    // If fitness facility doesn't exist (...it always should...), return.
    val hours = hoursFields ?: return hoursList

    hours.forEach { openHour ->
        if (openHour != null) {
            val startMillis: Long = openHour.startTime.toLong() * 1000
            val endMillis: Long = openHour.endTime.toLong() * 1000

            val startCal = Calendar.getInstance()
            startCal.timeInMillis = startMillis
            val endCal = Calendar.getInstance()
            endCal.timeInMillis = endMillis

            val day = when (startCal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> 6
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                else -> -1
            }

            val data = when (additionalData) {
                HourAdditionalData.NONE -> ""
                HourAdditionalData.WOMEN_ONLY -> (openHour.isWomen == true).toString()
                HourAdditionalData.COURT_TYPE -> openHour.courtType.toString()
            }.lowercase()

            val newTimeInterval = TimeInterval(
                start = TimeOfDay(
                    hour = startCal.get(Calendar.HOUR_OF_DAY),
                    minute = startCal.get(Calendar.MINUTE)
                ),
                end = TimeOfDay(
                    hour = endCal.get(Calendar.HOUR_OF_DAY),
                    minute = endCal.get(Calendar.MINUTE)
                )
            )

            // Initialize hours at index day if it doesn't have an entry.
            if (hoursList[day] == null) {
                hoursList[day] = mutableListOf()
            }

            // Know it is non-null from if statement above.
            hoursList[day]!!.add(Pair(newTimeInterval, data))
        }
    }

    for (i in 0 until hoursList.size) {
        if (hoursList[i] != null) {
            hoursList[i]!!.sortWith { h1, h2 ->
                h1.first.end.compareTo(h2.first.end)
            }
        }
    }

    return hoursList
}

/**
 * Returns the capacity at the given gym query.
 */
fun GymListQuery.GetAllGym.pullCapacity(
    facilityIn: GymFields.Facility?
): UpliftCapacity? {
    // If fitness facility doesn't exist (...it always should...), return.
    val facility = facilityIn ?: return null
    if (facility.facilityFields.capacity == null
        || facility.facilityFields.capacity.capacityFields.percent < 0.0
    ) return null

    val highCap = facility.facilityFields.capacity.capacityFields.let {
        it.count / it.percent
    }

    if (highCap.isNaN()) return null

    // Ex: "2023-09-19T18:42:00"
    val cal = Calendar.getInstance()
    cal.timeInMillis = facility.facilityFields.capacity.capacityFields.updated * 1000L

    return UpliftCapacity(
        percent = facility.facilityFields.capacity.capacityFields.percent,
        updated = cal
    )
}

/** Returns the equipment groupings for the given fitness facility. */
fun GymListQuery.GetAllGym.pullEquipmentGroupings(
    facilityIn: GymFields.Facility?
): List<EquipmentGrouping> {
    // TODO: Change to parse equipment grouping info when backend adds that.

    val facility = facilityIn ?: return listOf()


    val equipments = facilityIn.facilityFields.equipment ?: return listOf()

    val equipmentGroups = HashMap<EquipmentType, EquipmentGrouping>()
    equipments.forEach { equipment ->
        if (equipment != null) {
            val equipType = equipment.equipmentFields.equipmentType
            val equipmentF = EquipmentField(
                id = equipment.equipmentFields.id,
                accessibility = equipment.equipmentFields.accessibility,
                name = equipment.equipmentFields.name,
                facilityId = equipment.equipmentFields.facilityId,
                quantity = equipment.equipmentFields.quantity ?: 0,
            )

            if (equipmentGroups.containsKey(equipType)) {
                equipmentGroups[equipType]?.equipmentList?.add(equipmentF)
            } else {
                equipmentGroups[equipType] = EquipmentGrouping(
                    equipmentType = equipType,
                    equipmentList = ArrayList<EquipmentField>()
                )
                equipmentGroups[equipType]?.equipmentList?.add(equipmentF)
            }
        }
    }

    return equipmentGroups.values.toList()
}

/**
 * Returns the name of the given gym facility.
 *
 * Serves as a temporary fix to the gym vs. fitness facility debacle.
 */
fun GymFields.Facility.pullName(gymName: String): String {
    if (gymName.lowercase().contains("teagle")) {
        return if (facilityFields.name.lowercase().contains("up")) "Teagle Up"
        else "Teagle Down"
    }
    return gymName
}

/**
 * Returns a list of all the [UpliftGym]s that this gym query represents.
 *
 * Example: Teagle Gym in backend has both Teagle Up and Teagle Down as separate fitness centers.
 * This should separate them into distinct gyms.
 */
fun GymListQuery.GetAllGym.toUpliftGyms(): List<UpliftGym> {
    val fitnessFacilities = gymFields.facilities?.filterNotNull()?.filter { facility ->
        facility.facilityFields.facilityType.toString() == "FITNESS"
    } ?: listOf()

    val bowlingFacility = gymFields.facilities?.filterNotNull()?.firstOrNull { facility ->
        facility.facilityFields.facilityType.toString() == "BOWLING"
    }

    val poolFacility = gymFields.facilities?.filterNotNull()?.firstOrNull { facility ->
        facility.facilityFields.facilityType.toString() == "POOL"
    }

    val gyms = fitnessFacilities.map { facility ->
        UpliftGym(
            name = facility.pullName(gymFields.name),
            id = gymFields.id,
            facilityId = facility.facilityFields.id,
            popularTimes = pullPopularTimes(facility),
            // Need replace because there's a typo with the single quote.
            imageUrl = gymFields.imageUrl?.replace("'", "")
                ?.replace("toni-morrison-outside", "toni_morrison_outside")
                ?: defaultGymUrl,
            hours = pullHours(facility.facilityFields.hours?.map { it?.openHoursFields }).toTimeInterval(),
            equipmentGroupings = pullEquipmentGroupings(facility),
            miscellaneous = pullMiscellaneous(),
            bowlingInfo = bowlingFacility.pullBowling(),
            swimmingInfo = poolFacility.pullSwimmingInfo(),
            courtInfo = pullGymnasiumInfo(),
            upliftCapacity = pullCapacity(facility),
            latitude = gymFields.latitude,
            longitude = gymFields.longitude,
            amenities = gymFields.amenities
        )
    }

    return gyms
}
