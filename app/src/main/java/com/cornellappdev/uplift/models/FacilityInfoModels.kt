package com.cornellappdev.uplift.models

/**
 * A facility representing one court at a fitness center.
 */
data class CourtFacility(
    /** The court's name (e.g. "Court 1") */
    val name: String,
    /** Exactly 7 lists of [CourtTime] objects: Monday, Tuesday, etc. for this court. */
    val hours: List<List<CourtTime>?>
)

/** A court time interval which can be designated to a sport. */
data class CourtTime(
    val time: TimeInterval,
    val type: String
)

/** Swimming information for one day. */
data class SwimmingInfo(
    val swimmingTimes: List<SwimmingTime>
) {
    /**
     * Returns [swimmingTimes] as a list of [TimeInterval]s.
     */
    fun hours(): List<TimeInterval> {
        return swimmingTimes.map { swimTime -> swimTime.time }
    }
}

/** One time interval of swimming time which can be designated as women only. */
data class SwimmingTime(
    val time: TimeInterval,
    val womenOnly: Boolean
)

/** Bowling information for one day. */
data class BowlingInfo(
    val hours: List<TimeInterval>,
    val pricePerGame: String,
    val shoeRental: String
)

/** An [EquipmentGrouping] is a grouping of one or more pieces of gym equipment under a particular
 * category. [UpliftGym] objects may have multiple [EquipmentGrouping]s to specify all the equipment
 * they carry. */
data class EquipmentGrouping(
    /** The title of this equipment grouping. (e.g. "Cardio Machines") */
    val name: String,
    /** A list of equipment that this grouping offers.
     *
     * Example: The list (("Treadmills", 5), ("Rowing Machines", 3)) indicates that this grouping has
     * 5 treadmills and 3 rowing machines.
     * */
    val equipmentList: List<Pair<String, Int>>
)
