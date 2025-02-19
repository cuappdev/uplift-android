package com.cornellappdev.uplift.util.gymdetail

import com.cornellappdev.uplift.R

/**
 * A map from major muscle group to a list of sub-muscle groups.
 */
val majorToSubGroupMap = mapOf(
    ("Chest" to listOf("Chest")),
    ("Back" to listOf("Back")),
    ("Shoulders" to listOf("Shoulders")),
    ("Arms" to listOf("Biceps", "Triceps")),
    ("Legs" to listOf("Quads", "Hamstrings", "Glutes", "Calves")),
    ("Abdominals" to listOf("Abdominals")),
    ("Miscellaneous" to listOf("Cardio", "Miscellaneous"))
)

val defaultMuscleIconId = R.drawable.ic_miscellaneous_equip

/**
 * A map from major muscle group to an image id.
 */
val majorMuscleToImageId = mapOf(
    ("Chest" to R.drawable.ic_chest),
    ("Back" to R.drawable.ic_back),
    ("Shoulders" to R.drawable.ic_shoulders),
    ("Arms" to R.drawable.ic_arms),
    ("Legs" to R.drawable.ic_legs),
    ("Abdominals" to R.drawable.ic_abdominals),
    ("Miscellaneous" to defaultMuscleIconId),
)

/**
 * A map from gym amenity to a pair of an image id and a string.
 */
val amenityMap = mapOf(
    "SHOWERS" to Pair(R.drawable.ic_shower, "Showers"),
    "LOCKERS" to Pair(R.drawable.ic_lock, "Lockers"),
    "PARKING" to Pair(R.drawable.ic_parking, "Parking"),
    "ELEVATORS" to Pair(R.drawable.ic_elevator, "Elevators/Lifts")
)
