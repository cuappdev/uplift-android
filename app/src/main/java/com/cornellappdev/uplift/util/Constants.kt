package com.cornellappdev.uplift.util

import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.Sport

val sports : List<Sport> = listOf(
    Sport(painterId = R.drawable.ic_bowling_pins, name = "Bowling"),
    Sport(painterId = R.drawable.ic_dumbbell, name = "Lifting"),
    Sport(painterId = R.drawable.ic_basketball_hoop, name = "Basketball"),
    Sport(painterId = R.drawable.ic_swimming_pool, name = "Swimming"),
).sorted()