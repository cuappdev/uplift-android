package com.cornellappdev.uplift.models

data class UpliftActivity(
    val name: String,
    val id: String,
    /**
     * Track if user saved an Activity, default=False.
     */
    val starred: Boolean,

    /**
     * Id for element in location.
     */
    val gymId: String,
    /**
     * Address of element in location.
     */
    val address: String,

    /**
     * A list of exactly 7 lists of time intervals. Each list of time intervals corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday), and the times in said list indicates the hours of
     * this activity.
     *
     * Example: ((7:00 AM - 8:30 AM, 10:00AM - 10:45PM), ...) indicates that on Monday, this
     * activity is available from 7 to 8:30 AM, then unavailable, and then is available from 10 AM to 10:45 PM.
     *
     * If an index of this list is null, that indicates the activity is not available on that day.
     */
    val hours: List<List<TimeInterval>?>,

    /**
     * A list of (String, Int) pairs for pricing options eligible and corresponding costs.
     * eg:[("one-time", 3),("membership": 30)],
     * `null` means a free activity.
     */
    val pricing: List<Pair<String, Int>>?,

    /**
     * A list of equipment needed for activity. eg:['shoes','racket']
     */
    val equipment: List<Pair<String, Int>>?,

    /**
     * A list of services eligible. eg:['showers','lockers'], ['parking'].
     */
    val services: List<String>,

    /**
     * A set of gears option eligible. eg:['Not Required','Can Rent'], ['Bring Yourself'].
     */
    val gear: Set<Gear>,

    /**
     * A boolean to track if activity needs reservation.
     */
    val reservation: Boolean,
    val imageUrl: String,
)

enum class Gear {
    NOT_REQUIRED,
    BRING_YOURSELF,
    CAN_RENT
}
