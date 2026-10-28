package com.cornellappdev.uplift.data.repositories

import android.net.Uri
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.GetUserByNetIdQuery
import com.cornellappdev.uplift.GetWeeklyWorkoutDaysQuery
import com.cornellappdev.uplift.SetWorkoutGoalsMutation
import com.cornellappdev.uplift.data.models.ProfileData
import com.cornellappdev.uplift.data.models.WorkoutDomain
import com.cornellappdev.uplift.data.models.WorkoutGoal
import com.cornellappdev.uplift.fragment.UserFields
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Thrown when the backend rejects a goal change because the goal was changed in the last 30 days.
 */
class GoalLockedException : IllegalStateException("Workout goal is locked")

@Singleton
class ProfileRepository @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    @Named("main") private val apolloClient: ApolloClient
) {
    private val _goalUpdatedEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val goalUpdatedEvent: SharedFlow<Unit> = _goalUpdatedEvent.asSharedFlow()

    suspend fun updateProfilePhoto(uri: Uri): Result<Uri> = userInfoRepository.updateProfilePhoto(uri)

    suspend fun getProfile(): Result<ProfileData> = runCatching {
        val user = fetchCurrentUser()

        val userId = user.id.toIntOrNull()
            ?: throw IllegalStateException("Invalid user ID: ${user.id}")

        val weeklyResponse = apolloClient.query(GetWeeklyWorkoutDaysQuery(userId)).execute()

        if (weeklyResponse.hasErrors()) {
            throw IllegalStateException("Weekly workout days query failed: ${weeklyResponse.errors}")
        }

        val workouts = user.workoutHistory?.filterNotNull().orEmpty()

        val workoutDomain = workouts.map {
            WorkoutDomain(
                gymName = it.workoutFields.gymName,
                timestamp = Instant.parse(it.workoutFields.workoutTime.toString())
                    .toEpochMilli()
            )
        }

        val weeklyDays = weeklyResponse.data?.getWeeklyWorkoutDays?.filterNotNull().orEmpty()

        ProfileData(
            name = user.name,
            netId = user.netId,
            encodedImage = user.encodedImage,
            totalGymDays = user.totalGymDays,
            activeStreak = user.activeStreak,
            maxStreak = user.maxStreak,
            streakStart = user.streakStart?.toString(),
            workoutGoal = user.workoutGoal ?: 0,
            workouts = workoutDomain,
            weeklyWorkoutDays = weeklyDays
        )
    }.onFailure { e ->
        Log.e("ProfileRepo", "Failed to load profile", e)
    }

    suspend fun getWorkoutGoal(): Result<WorkoutGoal> = runCatching {
        fetchCurrentUser().toWorkoutGoal()
    }.onFailure { e ->
        Log.e("ProfileRepo", "Failed to load workout goal", e)
    }

    /**
     * Sets the user's workout goal and returns the updated goal from the backend. Fails with
     * [GoalLockedException] if the goal was changed in the last 30 days.
     */
    suspend fun setWorkoutGoal(goal: Int): Result<WorkoutGoal> = runCatching {
        val userId = userInfoRepository.getUserIdFromDataStore()?.toIntOrNull()
            ?: throw IllegalStateException("Missing user ID")

        val response = apolloClient
            .mutation(
                SetWorkoutGoalsMutation(
                    userId = userId,
                    workoutGoal = goal
                )
            )
            .execute()

        if (response.errors?.any { it.message.contains(GOAL_LOCKED_ERROR) } == true) {
            throw GoalLockedException()
        }
        if (response.hasErrors()) {
            throw IllegalStateException("Goal update failed: ${response.errors}")
        }

        // Apollo doesn't throw on network failures, so a missing user also means the update failed
        val updatedGoal = response.data?.setWorkoutGoals?.userFields?.toWorkoutGoal()
            ?: throw IllegalStateException("Goal update returned no user", response.exception)

        _goalUpdatedEvent.tryEmit(Unit)
        updatedGoal
    }.onFailure { e ->
        Log.e("ProfileRepo", "Failed to update workout goal", e)
    }

    private suspend fun fetchCurrentUser(): UserFields {
        val netId = userInfoRepository.getNetIdFromDataStore()
            ?: throw IllegalStateException("NetId missing")

        val userResponse = apolloClient.query(
            GetUserByNetIdQuery(netId)
        ).execute()

        if (userResponse.hasErrors()) {
            Log.e("ProfileRepo", "User query errors: ${userResponse.errors}")
            throw IllegalStateException("User query failed")
        }

        return userResponse.data?.getUserByNetId?.firstOrNull()?.userFields
            ?: throw IllegalStateException("User not found", userResponse.exception)
    }

    private fun UserFields.toWorkoutGoal(): WorkoutGoal = WorkoutGoal(
        goal = workoutGoal,
        // The backend stores this in UTC and sends it without an offset
        lastGoalChange = lastGoalChange?.let {
            LocalDateTime.parse(it.toString()).toInstant(ZoneOffset.UTC)
        }
    )

    private companion object {
        /** Part of the error message the backend sends when a goal is changed too soon. */
        const val GOAL_LOCKED_ERROR = "once every 30 days"
    }
}
