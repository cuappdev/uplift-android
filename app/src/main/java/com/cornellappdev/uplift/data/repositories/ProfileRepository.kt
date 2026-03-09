package com.cornellappdev.uplift.data.repositories

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.GetUserByNetIdQuery
import com.cornellappdev.uplift.GetWeeklyWorkoutDaysQuery
import com.cornellappdev.uplift.GetWorkoutsByIdQuery
import com.cornellappdev.uplift.SetWorkoutGoalsMutation
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

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

data class WorkoutDomain(
    val gymName: String,
    val timestamp: Long
)

@Singleton
class ProfileRepository @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val apolloClient: ApolloClient
) {
    suspend fun getProfile(): Result<ProfileData> {
        return try{
            val netId = userInfoRepository.getNetIdFromDataStore()
                ?: return Result.failure(Exception("NetId missing"))

            val userResponse = apolloClient.query(
                GetUserByNetIdQuery(netId)
            ).execute()

            if (userResponse.hasErrors()) {
                Log.e("ProfileRepo", "User query errors: ${userResponse.errors}")
                return Result.failure(IllegalStateException("User query failed"))
            }

            val user = userResponse.data?.getUserByNetId?.firstOrNull()?.userFields
                ?: return Result.failure(IllegalStateException("User not found"))

            val userId = user.id.toIntOrNull()
                ?: return Result.failure(IllegalStateException("Invalid user ID: ${user.id}"))

            val workoutResponse = apolloClient
                .query(GetWorkoutsByIdQuery(userId))
                .execute()

            if (workoutResponse.hasErrors()) {
                Log.e("ProfileRepo", "Workout query errors: ${workoutResponse.errors}")
            }

            val workouts = if (workoutResponse.hasErrors()) {
                emptyList()
            } else {
                workoutResponse.data?.getWorkoutsById?.filterNotNull() ?: emptyList()
            }

            val workoutDomain = workouts.map {
                WorkoutDomain(
                    gymName = it.workoutFields.gymName,
                    timestamp = Instant.parse(it.workoutFields.workoutTime.toString()).toEpochMilli()
                )
            }

            val weeklyResponse = apolloClient.query(GetWeeklyWorkoutDaysQuery(userId)).execute()
            if (weeklyResponse.hasErrors()) {
                Log.e("ProfileRepo", "Weekly query errors=${weeklyResponse.errors}")
            }

            val weeklyDays = if (weeklyResponse.hasErrors()) {
                emptyList()
            } else {
                weeklyResponse.data?.getWeeklyWorkoutDays?.filterNotNull() ?: emptyList()
            }

            Result.success(
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
            )
        } catch (e: Exception) {
        Log.e("ProfileRepo", "Failed to load profile", e)
            Result.failure(e)
        }
    }

    suspend fun setWorkoutGoal(userId: Int, goal: Int): Result<Unit> {
        return try {
            val response = apolloClient
                .mutation(
                    SetWorkoutGoalsMutation(
                        id = userId,
                        workoutGoal = goal
                    )
                )
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception("Goal update failed"))
            } else {
                Result.success(Unit)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}