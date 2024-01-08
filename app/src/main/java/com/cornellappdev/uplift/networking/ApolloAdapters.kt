package com.cornellappdev.uplift.networking

import com.cornellappdev.uplift.GymListQuery
import com.cornellappdev.uplift.fragment.GymFields
import com.cornellappdev.uplift.fragment.OpenHoursFields
import com.cornellappdev.uplift.models.BowlingInfo
import com.cornellappdev.uplift.models.EquipmentGrouping
import com.cornellappdev.uplift.models.GymnasiumInfo
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.models.SwimmingInfo
import com.cornellappdev.uplift.models.SwimmingTime
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftCapacity
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.util.defaultGymUrl
import java.util.Calendar

/**
 * Returns the popular times list representation for this gym query.
 */
fun GymListQuery.Gym.pullPopularTimes(
    facilityIn: GymFields.Facility?
): List<PopularTimes> {
    // TODO: Change to pull actual popular times info when backend adds that.
    return listOf()
}

/**
 * Returns the swimming info for this gym query.
 */
fun GymFields.Facility?.pullSwimmingInfo(): List<SwimmingInfo?>? {
    // If bowling facility doesn't exist, return.
    val hours = this?.facilityFields?.hours?.map {
        it?.openHoursFields
    } ?: return null

    val intervals = pullHours(hours)

    return intervals.map { list ->
        if (list != null)
            SwimmingInfo(list.map {
                // TODO: Not sure if women-only times are still a thing,
                //  but putting to only false for now.
                SwimmingTime(it, false)
            })
        else null
    }
}

/** Returns the gymnasium info for this gym query. */
fun GymListQuery.Gym.pullGymnasiumInfo(): List<GymnasiumInfo?>? {
    // TODO: Change to pull actual gymnasium info when backend adds that.
    return null
}

/** Returns the equipment groupings for the given fitness facility. */
fun GymListQuery.Gym.pullEquipmentGroupings(
    facilityIn: GymFields.Facility?
): List<EquipmentGrouping> {
    // TODO: Change to parse equipment grouping info when backend adds that.
    return listOf()
}

/**
 * Returns the miscellaneous details for this gym query.
 */
fun GymListQuery.Gym.pullMiscellaneous(): List<String> {
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

    val intervals = pullHours(hours)

    return intervals.map {
        if (it != null)
            BowlingInfo(it, "$4.00", "$3.00")
        else null
    }
}

/**
 * Returns the hours for the given open hour list. Works for any open hour list.
 */
fun pullHours(
    hoursFields: List<OpenHoursFields?>?
): List<List<TimeInterval>?> {
    // Initialize to always closed.
    val hoursList: MutableList<MutableList<TimeInterval>?> = MutableList(7) { null }

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

            // Initialize hours at index day if it doesn't have an entry.
            if (hoursList[day] == null) {
                hoursList[day] = mutableListOf()
            }

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

            // Know it is non-null from if statement above.
            hoursList[day]!!.add(newTimeInterval)
        }
    }

    for (i in 0 until hoursList.size) {
        if (hoursList[i] != null) {
            hoursList[i]!!.sortWith { h1, h2 ->
                h1.end.compareTo(h2.end)
            }
        }
    }

    return hoursList
}

/**
 * Returns the capacity at the given gym query.
 */
fun GymListQuery.Gym.pullCapacity(
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
fun GymListQuery.Gym.toUpliftGyms(): List<UpliftGym> {
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
            hours = pullHours(facility.facilityFields.hours?.map { it?.openHoursFields }),
            equipmentGroupings = pullEquipmentGroupings(facility),
            miscellaneous = pullMiscellaneous(),
            bowlingInfo = bowlingFacility.pullBowling(),
            swimmingInfo = poolFacility.pullSwimmingInfo(),
            gymnasiumInfo = pullGymnasiumInfo(),
            upliftCapacity = pullCapacity(facility),
            latitude = gymFields.latitude,
            longitude = gymFields.longitude
        )
    }

    return gyms
}
