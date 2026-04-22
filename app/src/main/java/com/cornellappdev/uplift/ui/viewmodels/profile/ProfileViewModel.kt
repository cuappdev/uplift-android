package com.cornellappdev.uplift.ui.viewmodels.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.ProfileRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItem
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.timeAgoString
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

sealed class HistoryListItem {
    data class Header(val month: String) : HistoryListItem()
    data class Workout(
        val item: HistoryItem,
        val showDivider: Boolean
    ) : HistoryListItem()
    data class SpacerItem(val month: String) : HistoryListItem()
}
data class ProfileUiState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val name: String = "",
    val netId: String = "",
    val profileImage: Uri? = null,
    val totalGymDays: Int = 0,
    val activeStreak: Int = 0,
    val maxStreak: Int = 0,
    val streakStart: String? = null,
    val workoutGoal: Int = 0,
    val historyItems: List<HistoryItem> = emptyList(),
    val daysOfMonth: List<Int> = emptyList(),
    val completedDays: List<Boolean> = emptyList(),
    val workoutsCompleted: Int = 0,
    val historyListItems: List<HistoryListItem> = emptyList(),
    val workoutDates: Map<LocalDate, List<HistoryItem>> = emptyMap()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val rootNavigationRepository: RootNavigationRepository,
) : UpliftViewModel<ProfileUiState>(ProfileUiState()) {

    private var loadingJob: Job? = null

    init {
        reload()
    }

    fun reload() {
        if (loadingJob?.isActive == true) return
        loadingJob = loadProfile()

    }


    private fun loadProfile(): Job = viewModelScope.launch {
        applyMutation { copy(loading = true, error = false) }

        val result = profileRepository.getProfile()

        val profile = result.getOrNull()
        if (profile == null) {
            Log.e("profile VM", "Failed to load profile", result.exceptionOrNull())
            applyMutation { copy(loading = false, error = true) }
            return@launch
        }
        val historyItems = profile.workouts.map {
            val workoutInstant = Instant.ofEpochMilli(it.timestamp)
            val calendar = java.util.Calendar.getInstance().apply {
                timeInMillis = it.timestamp
            }
            HistoryItem(
                gymName = it.gymName,
                time = formatTime.format(workoutInstant),
                date = formatDate.format(workoutInstant),
                timestamp = it.timestamp,
                ago = calendar.timeAgoString()
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

        val grouped = historyItems
            .sortedByDescending { it.timestamp }
            .groupBy {
                Instant.ofEpochMilli(it.timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US))
            }

        val historyListItems = buildList{
            grouped.forEach { (month, items) ->
                add(HistoryListItem.Header(month))
                items.forEachIndexed { index, item ->
                    add(
                        HistoryListItem.Workout(
                            item = item,
                            showDivider = index < items.lastIndex
                        )
                    )
                }
                add(HistoryListItem.SpacerItem(month))
            }
        }

        val workoutDates = historyItems.groupBy {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        applyMutation {
            copy(
                loading = false,
                name = profile.name,
                netId = profile.netId,
                profileImage = profile.encodedImage?.let(Uri::parse),
                totalGymDays = profile.totalGymDays,
                activeStreak = profile.activeStreak,
                maxStreak = profile.maxStreak,
                streakStart = profile.streakStart,
                workoutGoal = profile.workoutGoal,
                historyItems = historyItems,
                daysOfMonth = daysOfMonth,
                completedDays = completedDays,
                workoutsCompleted = workoutsCompleted,
                historyListItems = historyListItems,
                workoutDates = workoutDates
            )

        }
    }

    fun updateWorkoutGoal(goal: Int) = viewModelScope.launch {
        val result = profileRepository.setWorkoutGoal(goal)

        if (result.isSuccess) {
            reload()
        } else {
            Log.e("profile VM", "Failed to update workout goal", result.exceptionOrNull())
        }
    }

    fun toSettings() {
        rootNavigationRepository.navigate(UpliftRootRoute.Settings)
    }

    fun toGoals() {
        // replace with the actual route once goals exists
    }

    fun toHistory() {
        rootNavigationRepository.navigate(UpliftRootRoute.WorkoutHistory)
    }



    private val formatTime = DateTimeFormatter
        .ofPattern("h:mm a")
        .withLocale(Locale.US)
        .withZone(ZoneId.systemDefault())

    private val formatDate = DateTimeFormatter
        .ofPattern("MMMM d, yyyy")
        .withLocale(Locale.US)
        .withZone(ZoneId.systemDefault())

    private val formatDayOfWeek = DateTimeFormatter
        .ofPattern("EEE")
        .withLocale(Locale.US)
        .withZone(ZoneId.systemDefault())
}
