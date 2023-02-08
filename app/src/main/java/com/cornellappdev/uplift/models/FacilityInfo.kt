package com.cornellappdev.uplift.models

data class GymnasiumInfo(
    val hours : List<TimeInterval>,
    val courts : List<CourtInfo>
)

data class CourtInfo(
    val name : String,
    val hours : List<TimeInterval>
)

data class SwimmingInfo(
    val swimmingTimes : List<SwimmingTime>
)

data class SwimmingTime(
    val time : TimeInterval,
    val womenOnly : Boolean
)

data class BowlingInfo(
    val hours : List<TimeInterval>,
    val pricePerGame : String,
    val shoeRental : String
)