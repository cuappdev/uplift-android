package com.cornellappdev.uplift.data.models
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val name: String,
    val netId: String,
    val id: String,
    val workoutGoal: String,
    val workoutTime: String
)