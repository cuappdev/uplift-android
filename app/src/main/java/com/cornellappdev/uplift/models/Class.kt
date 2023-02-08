package com.cornellappdev.uplift.models

import java.util.*

data class Class(
    val name: String,
    val location: String,
    val instructorName: String,
    val minutes: Int,
    val date: Calendar,
    val time: TimeInterval,
    val functions : List<String>,
    val preparation : String,
    val description : String,
    val nextSessions : List<Class>
)