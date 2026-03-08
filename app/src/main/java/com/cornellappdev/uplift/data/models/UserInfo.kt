package com.cornellappdev.uplift.data.models
import com.cornellappdev.uplift.type.DateTime
import kotlinx.serialization.Serializable


@Serializable
data class UserInfo(
    val id: String,
    val email: String,
    val name: String,
    val netId: String,
    val encodedImage: String?,
    val activeStreak: Int?,
    val maxStreak: Int?,
    val streakStart: String?,
    val workoutGoal: Int?,
    val totalGymDays: Int
)