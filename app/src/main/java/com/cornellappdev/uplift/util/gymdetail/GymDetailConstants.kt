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

/**
 * A map from major muscle group to an image id.
 */
val majorMuscleToImageId = mapOf(
    ("Chest" to R.drawable.ic_miscellaneous_equip),
    ("Back" to R.drawable.ic_miscellaneous_equip),
    ("Shoulders" to R.drawable.ic_miscellaneous_equip),
    ("Arms" to R.drawable.ic_miscellaneous_equip),
    ("Legs" to R.drawable.ic_miscellaneous_equip),
    ("Abdominals" to R.drawable.ic_miscellaneous_equip),
    ("Miscellaneous" to R.drawable.ic_miscellaneous_equip)
)