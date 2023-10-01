package com.cornellappdev.uplift.networking

import com.cornellappdev.uplift.models.BowlingInfo
import com.cornellappdev.uplift.models.EquipmentGrouping
import com.cornellappdev.uplift.models.GymnasiumInfo
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.models.SwimmingInfo
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftCapacity
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.util.defaultGymUrl
import com.example.rocketreserver.GymListQuery
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Returns the popular times list representation for this gym query.
 */
fun GymListQuery.Gym.pullPopularTimes(
    facilityIn: GymListQuery.Facility? = getFitnessFacility()
): List<PopularTimes> {
    return listOf()
}

/**
 * Returns the swimming info for this gym query.
 */
fun GymListQuery.Gym.pullSwimmingInfo(): List<SwimmingInfo?>? {
    return null
}

/** Returns the gymnasium info for this gym query. */
fun GymListQuery.Gym.pullGymnasiumInfo(): List<GymnasiumInfo?>? {
    // TODO: Change to pull actual gymnasium info when backend adds that.
    return null
}

/** Returns the equipment groupings for the given gym query. */
fun GymListQuery.Gym.pullEquipmentGroupings(
    facilityIn: GymListQuery.Facility? = getFitnessFacility()
): List<EquipmentGrouping> {
    // TODO: Change to parse equipment grouping info when backend adds that.
    return listOf()
}

/**
 * Returns the miscellaneous details for this gym query.
 */
fun GymListQuery.Gym.pullMiscellaneous(): List<String> {
    return listOf()
}

/**
 * Returns the bowling info for this gym query.
 */
fun GymListQuery.Gym.pullBowling(): List<BowlingInfo?>? {
    return null
}

/**
 * Returns the hours for the given gym query.
 */
fun GymListQuery.Gym.pullHours(
    facilityIn: GymListQuery.Facility? = getFitnessFacility()
): List<List<TimeInterval>?> {
    // Initialize to always closed.
    val hours: MutableList<MutableList<TimeInterval>?> = MutableList(7) { null }

    // If fitness facility doesn't exist (...it always should...), return.
    val facility = facilityIn ?: return hours

    facility.openHours?.forEach { openHour ->
        if (openHour != null) {
            val day = openHour.day

            // Initialize hours at index day if it doesn't have an entry.
            if (hours[day] == null) {
                hours[day] = mutableListOf()
            }

            val newTimeInterval = TimeInterval(
                start = TimeOfDay(
                    hour = openHour.startTime.toInt(),
                    minute = ((openHour.startTime - openHour.startTime.toInt()) * 60).roundToInt()
                ),
                end = TimeOfDay(
                    hour = openHour.endTime.toInt(),
                    minute = ((openHour.endTime - openHour.endTime.toInt()) * 60).roundToInt()
                )
            )

            // Know it is non-null from if statement above.
            hours[day]!!.add(newTimeInterval)
        }
    }

    for (i in 0 until 7) {
        hours[i]!!.sortWith { h1, h2 ->
            h1.end.compareTo(h2.end)
        }
    }

    return hours
}

/**
 * Returns the capacity at the given gym query.
 */
fun GymListQuery.Gym.pullCapacity(
    facilityIn: GymListQuery.Facility? = getFitnessFacility()
): UpliftCapacity? {
    // If fitness facility doesn't exist (...it always should...), return.
    val facility = facilityIn ?: return null
    if (facility.capacity == null || facility.capacity.percent < 0.0) return null

    val highCap = facility.capacity.count / facility.capacity.percent

    if (highCap.isNaN()) return null

    // Ex: "2023-09-19T18:42:00"
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    cal.time = sdf.parse(facility.capacity.updated.toString()) ?: Calendar.getInstance().time

    return UpliftCapacity(
        percent = facility.capacity.percent,
        updated = cal
    )
}

/**
 * Returns the "FITNESS" facility at a gym. `null` if the gym does not have it.
 */
private fun GymListQuery.Gym.getFitnessFacility(): GymListQuery.Facility? {
    return facilities?.find { facility ->
        facility?.facilityType.toString() == "FITNESS"
    }
}

/**
 * Returns a list of all the [UpliftGym]s that this gym query represents.
 *
 * Example: Teagle Gym in backend has both Teagle Up and Teagle Down as separate fitness centers.
 * This should separate them into distinct gyms.
 */
fun GymListQuery.Gym.toUpliftGyms(): List<UpliftGym> {
    val fitnessFacilities = facilities?.filter { facility ->
        facility?.facilityType.toString() == "FITNESS"
    } ?: listOf()

    return fitnessFacilities.filterNotNull().map { facility ->
        UpliftGym(
            name = facility.name,
            id = facility.id,
            popularTimes = pullPopularTimes(facility),
            // Need replace because there's a typo with the single quote.
            imageUrl = imageUrl?.replace("'", "") ?: defaultGymUrl,
            hours = pullHours(facility),
            equipmentGroupings = pullEquipmentGroupings(facility),
            miscellaneous = pullMiscellaneous(),
            bowlingInfo = pullBowling(),
            // No swimming info in backend yet...
            swimmingInfo = pullSwimmingInfo(),
            gymnasiumInfo = pullGymnasiumInfo(),
            // TODO: Reflect actual capacity pulled from backend.
            upliftCapacity = pullCapacity(facility)
        )
    }
}

/*
fun ClassListQuery.Class.toUpliftClass(): UpliftClass {
    return UpliftClass(
        name = details.name,
        id = id,
        gymId = gymId ?: "NO_GYM",
        location = location,
        instructorName = instructor,
        date = parseDate(date.toString()),
        time = TimeInterval(
            parseTimeOfDay(startTime.toString()),
            parseTimeOfDay(endTime.toString())
        ),
        functions = details.tags.filterNotNull().map { tag -> tag.label },
        // Preparation is not supplied by backend yet...
        preparation = "",
        description = details.description,
        imageUrl = imageUrl
    )
}
*/
