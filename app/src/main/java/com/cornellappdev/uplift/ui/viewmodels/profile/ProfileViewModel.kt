package com.cornellappdev.uplift.ui.viewmodels.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.ProfileRepository
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItem
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class ProfileUiState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val name: String = "",
    val netId: String = "",
    val profileImage: String? = null,
    val totalGymDays: Int = 0,
    val activeStreak: Int = 0,
    val maxStreak: Int = 0,
    val streakStart: String? = null,
    val workoutGoal: Int = 0,
    val historyItems: List<HistoryItem> = emptyList(),
    val daysOfMonth: List<Int> = emptyList(),
    val completedDays: List<Boolean> = emptyList(),
    val workoutsCompleted: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userInfoRepository: UserInfoRepository,
) : UpliftViewModel<ProfileUiState>(ProfileUiState()) {

    private var loadingJob: Job? = null

    fun reload() {
        if (loadingJob?.isActive == true) return
        loadingJob = loadProfile()

    }


    private fun loadProfile(): Job = viewModelScope.launch {
        applyMutation { copy(loading = true, error = false) }

        val result = profileRepository.getProfile()

        if (result.isSuccess) {
            val profile = result.getOrNull()!!

            val historyItems = profile.workouts.map {
                HistoryItem(
                    gymName = it.gymName,
                    time = formatTime.format(
                        Instant.ofEpochMilli(it.timestamp)
                    ),
                    date = formatDate.format(
                        Instant.ofEpochMilli(it.timestamp)
                    ),
                    timestamp = it.timestamp
                )
            }

            val now = LocalDate.now()
            val startOfWeek = now.with(DayOfWeek.MONDAY)

            val weekDates = (0..6).map {
                startOfWeek.plusDays(it.toLong())
            }

            val daysOfMonth = weekDates.map { it.dayOfMonth }

            val completedDays = weekDates.map { date ->
                profile.weeklyWorkoutDays.contains(date.toString())
            }

            val workoutsCompleted = profile.weeklyWorkoutDays.size

            applyMutation {
                copy(
                    loading = false,
                    name = profile.name,
                    netId = profile.netId,
                    profileImage = profile.encodedImage,
                    totalGymDays = profile.totalGymDays,
                    activeStreak = profile.activeStreak,
                    maxStreak = profile.maxStreak,
                    streakStart = profile.streakStart,
                    workoutGoal = profile.workoutGoal,
                    historyItems = historyItems,
                    daysOfMonth = daysOfMonth,
                    completedDays = completedDays,
                    workoutsCompleted = workoutsCompleted
                )
            }
        } else {
            Log.e("profile VM", "Failed to load profile", result.exceptionOrNull())
            applyMutation { copy(loading = false, error = true) }
        }

    }


    fun updateWorkoutGoal(goal: Int) = viewModelScope.launch {
        val userId = userInfoRepository.getUserIdFromDataStore()?.toIntOrNull() ?: return@launch
        val result = profileRepository.setWorkoutGoal(userId, goal)

        if (result.isSuccess) {
            reload()
        } else {
            Log.e("profile VM", "Failed to update workout goal", result.exceptionOrNull())
        }
    }

    private val formatTime = DateTimeFormatter
        .ofPattern("h:mm a")
        .withLocale(Locale.US)
        .withZone(ZoneId.systemDefault())

    private val formatDate = DateTimeFormatter
        .ofPattern("MMMM d, yyyy")
        .withLocale(Locale.US)
        .withZone(ZoneId.systemDefault())
}