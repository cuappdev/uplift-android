package com.cornellappdev.uplift.models

/** Gymnasium info for one day. Gymnasiums have can have one or more courts open per day. */
data class GymnasiumInfo(
    val hours: List<TimeInterval>,
    val courts: List<CourtInfo>
)

/** One court's name and hours. */
data class CourtInfo(
    val name: String,
    val hours: List<TimeInterval>
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
 * category. [Gym] objects may have multiple [EquipmentGrouping]s to specify all the equipment
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