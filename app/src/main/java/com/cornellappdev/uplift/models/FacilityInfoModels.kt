package com.cornellappdev.uplift.models

/** Gymnasium info for one day. Gymnasiums have can have one or more courts open per day. */
data class GymnasiumInfo(
    val hours : List<TimeInterval>,
    val courts : List<CourtInfo>
)

/** One court's name and hours. */
data class CourtInfo(
    val name : String,
    val hours : List<TimeInterval>
)

/** Swimming information for one day. */
data class SwimmingInfo(
    val swimmingTimes : List<SwimmingTime>
)

/** One time interval of swimming time which can be designated as women only. */
data class SwimmingTime(
    val time : TimeInterval,
    val womenOnly : Boolean
)

/** Bowling information for one day. */
data class BowlingInfo(
    val hours : List<TimeInterval>,
    val pricePerGame : String,
    val shoeRental : String
)