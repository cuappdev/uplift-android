package com.cornellappdev.uplift.data.repositories

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.GetUserByNetIdQuery
import com.cornellappdev.uplift.GetWeeklyWorkoutDaysQuery
import com.cornellappdev.uplift.GetWorkoutsByIdQuery
import com.cornellappdev.uplift.SetWorkoutGoalsMutation
import com.cornellappdev.uplift.data.models.ProfileData
import com.cornellappdev.uplift.data.models.WorkoutDomain
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileRepository @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val apolloClient: ApolloClient
) {
    suspend fun getProfile(): Result<ProfileData> = runCatching {
        val netId = userInfoRepository.getNetIdFromDataStore()
            ?: throw IllegalStateException("NetId missing")

        val userResponse = apolloClient.query(
            GetUserByNetIdQuery(netId)
        ).execute()

        if (userResponse.hasErrors()) {
            Log.e("ProfileRepo", "User query errors: ${userResponse.errors}")
            throw IllegalStateException("User query failed")
        }

        val user = userResponse.data?.getUserByNetId?.firstOrNull()?.userFields
            ?: throw IllegalStateException("User not found")

        val userId = user.id.toIntOrNull()
            ?: throw IllegalStateException("Invalid user ID: ${user.id}")

        coroutineScope {
            val workoutDeferred = async {
                apolloClient.query(GetWorkoutsByIdQuery(userId)).execute()
            }
            val weeklyDeferred = async {
                apolloClient.query(GetWeeklyWorkoutDaysQuery(userId)).execute()
            }

            val workoutResponse = workoutDeferred.await()
            if (workoutResponse.hasErrors()) {
                Log.e("ProfileRepo", "Workout query errors: ${workoutResponse.errors}")
                throw IllegalStateException("Workout query failed")
            }

            val workouts = workoutResponse.data?.getWorkoutsById?.filterNotNull().orEmpty()

            val workoutDomain = workouts.map {
                WorkoutDomain(
                    gymName = it.workoutFields.gymName,
                    timestamp = Instant.parse(it.workoutFields.workoutTime.toString())
                        .toEpochMilli()
                )
            }

            val weeklyResponse = weeklyDeferred.await()
            if (weeklyResponse.hasErrors()) {
                Log.e("ProfileRepo", "Weekly query errors: ${weeklyResponse.errors}")
                throw IllegalStateException("Weekly workout days query failed")
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
        }
    }.onFailure { e ->
        Log.e("ProfileRepo", "Failed to load profile", e)
    }

    suspend fun setWorkoutGoal(goal: Int): Result<Unit> = runCatching {
        val userId = userInfoRepository.getUserIdFromDataStore()?.toIntOrNull()
            ?: throw IllegalStateException("Missing user ID")

        val response = apolloClient
            .mutation(
                SetWorkoutGoalsMutation(
                    id = userId,
                    workoutGoal = goal
                )
            )
            .execute()

        if (response.hasErrors()) {
            throw IllegalStateException("Goal update failed")
        }
    }.onFailure { e ->
        Log.e("ProfileRepo", "Failed to update workout goal", e)
    }
}
