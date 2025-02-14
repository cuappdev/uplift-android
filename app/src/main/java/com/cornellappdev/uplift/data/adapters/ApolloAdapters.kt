package com.cornellappdev.uplift.data.adapters


import com.cornellappdev.uplift.ClassListQuery
import com.cornellappdev.uplift.GymListQuery
import com.cornellappdev.uplift.fragment.GymFields
import com.cornellappdev.uplift.fragment.OpenHoursFields
import com.cornellappdev.uplift.data.models.gymdetail.BowlingInfo
import com.cornellappdev.uplift.data.models.gymdetail.CourtFacility
import com.cornellappdev.uplift.data.models.gymdetail.CourtTime
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentField
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentGrouping
import com.cornellappdev.uplift.data.models.gymdetail.PopularTimes
import com.cornellappdev.uplift.data.models.gymdetail.SwimmingInfo
import com.cornellappdev.uplift.data.models.gymdetail.SwimmingTime
import com.cornellappdev.uplift.data.models.gymdetail.TimeInterval
import com.cornellappdev.uplift.data.models.gymdetail.TimeOfDay
import com.cornellappdev.uplift.data.models.UpliftCapacity
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentCategory
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentInfo
import com.cornellappdev.uplift.data.models.gymdetail.GymEquipmentGroupInfo
import com.cornellappdev.uplift.type.MuscleGroup
import com.cornellappdev.uplift.util.asTimeOfDay
import com.cornellappdev.uplift.util.defaultClassUrl
import com.cornellappdev.uplift.util.defaultGymUrl
import com.cornellappdev.uplift.util.gymdetail.defaultMuscleIconId
import com.cornellappdev.uplift.util.gymdetail.majorMuscleToImageId
import com.cornellappdev.uplift.util.gymdetail.majorToSubGroupMap
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Returns the popular times list representation for this gym query.
 */
fun pullPopularTimes(
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
fun pullMiscellaneous(): List<String> {
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
fun pullCapacity(
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

/**
 * Returns a list of [GymEquipmentGroupInfo]s based on the [equipmentGroupingMap].
 * @param equipmentGroupingMap a map of [MuscleGroup] to [EquipmentGrouping]
 * see [EquipmentCategory] for more info about the type of subGroupEquipmentsPairList
 * @return a list of [GymEquipmentGroupInfo]s
 */
private fun getEquipmentGroupInfoList(
    equipmentGroupingMap: HashMap<MuscleGroup, EquipmentGrouping>,
): List<GymEquipmentGroupInfo> {
    return majorToSubGroupMap.map { (majorGroup, subGroups) ->
        val subGroupEquipmentsPairList = subGroups.map { subGroup ->
            val equipmentList =
                equipmentGroupingMap[MuscleGroup.valueOf(subGroup.uppercase())]?.equipmentList
            val equipmentInfoList =
                equipmentList?.map { EquipmentInfo(it.name, it.quantity) } ?: emptyList()
            EquipmentCategory(subGroup, equipmentInfoList)
        }
        GymEquipmentGroupInfo(
            majorGroup,
            majorMuscleToImageId[majorGroup] ?: defaultMuscleIconId,
            subGroupEquipmentsPairList
        )
    }
}

/** Returns the equipment groupings for the given fitness facility. */
fun mapFacilityToEquipmentGroupInfoList(
    facilityIn: GymFields.Facility?
): List<GymEquipmentGroupInfo> {
    val equipments = facilityIn?.facilityFields?.equipment ?: return listOf()

    val equipmentGroups = HashMap<MuscleGroup, EquipmentGrouping>()
    equipments.forEach { equipment ->
        if (equipment != null) {
            val muscleGroups = equipment.equipmentFields.muscleGroups
            val equipmentField = EquipmentField(
                id = equipment.equipmentFields.id,
                accessibility = equipment.equipmentFields.accessibility,
                name = equipment.equipmentFields.name,
                facilityId = equipment.equipmentFields.facilityId,
                quantity = equipment.equipmentFields.quantity ?: 0,
                muscleGroups = muscleGroups.filterNotNull(),
                cleanName = equipment.equipmentFields.cleanName
            )
            muscleGroups.forEach { muscleGroup ->
                if (equipmentGroups.containsKey(muscleGroup)) {
                    equipmentGroups[muscleGroup]?.equipmentList?.add(equipmentField)
                } else if (muscleGroup != null) {
                    equipmentGroups[muscleGroup] = EquipmentGrouping(
                        equipmentType = muscleGroup,
                        equipmentList = ArrayList()
                    )
                    equipmentGroups[muscleGroup]?.equipmentList?.add(equipmentField)
                }
            }
        }
    }

    return getEquipmentGroupInfoList(equipmentGroups)
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
 * Returns a [Calendar] whose time is set to the time input, given in format:
 *
 * "yyyy-MM-dd'T'HH:mm:ss"
 *
 * Requires that the date is in the above format.
 */
fun parseCalendar(dateString: String): Calendar {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    cal.time = sdf.parse(dateString) ?: Calendar.getInstance().time

    return cal
}

fun ClassListQuery.Class.toUpliftClass(imageUrl: String = defaultClassUrl): UpliftClass? {
    try {
        val start = parseCalendar(startTime.toString())
        val end = parseCalendar(endTime.toString())

        return UpliftClass(
            name = class_?.name ?: "NO_NAME",
            id = id,
            gymId = gymId?.toString() ?: "NO_GYM",
            location = location,
            instructorName = instructor,
            date = end,
            time = TimeInterval(
                start.asTimeOfDay(),
                end.asTimeOfDay()
            ),
            // TODO: Functions aren't in backend yet (as far as I can tell). Update when added.
            functions = listOf(),
            // TODO: Preparation is not supplied by backend yet. Update when added.
            preparation = "",
            description = class_?.description ?: "NO_DESC",
            imageUrl = imageUrl.replace("'", ""),
        )
    } catch (_: ParseException) {
        return null
    }
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

    val courtInfo = pullGymnasiumInfo()

    val swimmingInfo = poolFacility.pullSwimmingInfo()

    val bowlingInfo = bowlingFacility.pullBowling()

    val miscellaneousInfo = pullMiscellaneous()

    val hasOneFacility =
        courtInfo.isNotEmpty() || swimmingInfo != null || bowlingInfo != null || miscellaneousInfo.isNotEmpty()

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
            equipmentGroupings = mapFacilityToEquipmentGroupInfoList(facility),
            miscellaneous = miscellaneousInfo,
            bowlingInfo = bowlingInfo,
            swimmingInfo = swimmingInfo,
            courtInfo = courtInfo,
            upliftCapacity = pullCapacity(facility),
            latitude = gymFields.latitude,
            longitude = gymFields.longitude,
            amenities = gymFields.amenities,
            hasOneFacility = hasOneFacility
        )
    }

    return gyms
}
