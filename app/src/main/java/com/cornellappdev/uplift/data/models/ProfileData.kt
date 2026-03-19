package com.cornellappdev.uplift.data.models

import android.net.Uri


data class ProfileData(
    val name: String,
    val netId: String,
    val encodedImage: String?,
    val totalGymDays: Int,
    val activeStreak: Int,
    val maxStreak: Int,
    val streakStart: String?,
    val workoutGoal: Int,
    val workouts: List<WorkoutDomain>,
    val weeklyWorkoutDays: List<String>
)