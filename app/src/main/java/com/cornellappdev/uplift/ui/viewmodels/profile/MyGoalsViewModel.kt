package com.cornellappdev.uplift.ui.viewmodels.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.models.WorkoutGoal
import com.cornellappdev.uplift.data.repositories.GoalLockedException
import com.cornellappdev.uplift.data.repositories.ProfileRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

sealed class GoalsLoadState {
    data object Loading : GoalsLoadState()
    data object Error : GoalsLoadState()
    data object Loaded : GoalsLoadState()
}

sealed class GoalSaveState {
    data object Idle : GoalSaveState()
    data object Saving : GoalSaveState()
    data object Saved : GoalSaveState()
    data object Error : GoalSaveState()
}

/**
 * @param savedGoal the goal stored on the backend, or null if the user hasn't set one
 * @param selectedGoal the goal currently selected on the slider
 * @param lockedUntilDate formatted date the goal can next be changed, or null if it isn't locked
 * @param lockedDaysRemaining days until the goal can next be changed
 */
data class MyGoalsUiState(
    val loadState: GoalsLoadState = GoalsLoadState.Loading,
    val savedGoal: Int? = null,
    val selectedGoal: Int = DEFAULT_GOAL,
    val saveState: GoalSaveState = GoalSaveState.Idle,
    val lockedUntilDate: String? = null,
    val lockedDaysRemaining: Int = 0,
    val showConfirmationDialog: Boolean = false,
    val showLockedDialog: Boolean = false
) {
    val isLocked: Boolean get() = lockedUntilDate != null

    // A user without a goal can save right away, even with the default selected
    val hasChanged: Boolean get() = selectedGoal != savedGoal

    val canSave: Boolean
        get() = loadState == GoalsLoadState.Loaded && !isLocked && hasChanged &&
                saveState != GoalSaveState.Saving
}

private const val DEFAULT_GOAL = 1

@HiltViewModel
class MyGoalsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : UpliftViewModel<MyGoalsUiState>(MyGoalsUiState()) {

    private var loadingJob: Job? = null

    init {
        reload()
    }

    fun reload() {
        if (loadingJob?.isActive == true) return
        loadingJob = loadGoal()
    }

    private fun loadGoal(): Job = viewModelScope.launch {
        applyMutation { copy(loadState = GoalsLoadState.Loading) }

        val goal = profileRepository.getWorkoutGoal().getOrNull()
        if (goal == null) {
            applyMutation { copy(loadState = GoalsLoadState.Error) }
            return@launch
        }

        applyGoal(goal)
        applyMutation {
            copy(loadState = GoalsLoadState.Loaded, showLockedDialog = isLocked)
        }
    }

    fun onGoalChange(goal: Int) {
        val state = getStateValue()
        if (state.loadState != GoalsLoadState.Loaded || state.saveState == GoalSaveState.Saving) {
            return
        }
        if (state.isLocked) {
            applyMutation { copy(showLockedDialog = true) }
            return
        }
        applyMutation { copy(selectedGoal = goal, saveState = GoalSaveState.Idle) }
    }

    fun onSaveClick() {
        if (getStateValue().canSave) {
            applyMutation { copy(showConfirmationDialog = true) }
        }
    }

    fun onDismissConfirmation() {
        applyMutation { copy(showConfirmationDialog = false) }
    }

    fun onDismissLocked() {
        applyMutation { copy(showLockedDialog = false) }
    }

    fun onConfirmSave() {
        val state = getStateValue()
        if (!state.canSave) return
        applyMutation { copy(showConfirmationDialog = false, saveState = GoalSaveState.Saving) }

        viewModelScope.launch {
            val result = profileRepository.setWorkoutGoal(state.selectedGoal)
            val goal = result.getOrNull()
            when {
                goal != null -> {
                    applyGoal(goal)
                    applyMutation { copy(saveState = GoalSaveState.Saved) }
                }

                result.exceptionOrNull() is GoalLockedException -> onSaveRejectedAsLocked()

                else -> applyMutation { copy(saveState = GoalSaveState.Error) }
            }
        }
    }

    /**
     * The backend says the goal is locked even though we thought it wasn't, so our copy is stale.
     * Reload it to get the real lock date and show the locked dialog.
     */
    private suspend fun onSaveRejectedAsLocked() {
        val goal = profileRepository.getWorkoutGoal().getOrNull()
        if (goal == null) {
            applyMutation { copy(saveState = GoalSaveState.Error) }
            return
        }
        applyGoal(goal)
        applyMutation {
            if (isLocked) {
                copy(saveState = GoalSaveState.Idle, showLockedDialog = true)
            } else {
                Log.e("MyGoalsViewModel", "Backend rejected goal as locked but lock date is past")
                copy(saveState = GoalSaveState.Error)
            }
        }
    }

    private fun applyGoal(goal: WorkoutGoal) {
        val lock = goal.lockAt(Instant.now())
        applyMutation {
            copy(
                savedGoal = goal.goal,
                selectedGoal = goal.goal ?: DEFAULT_GOAL,
                lockedUntilDate = lock?.unlocksAt?.let(lockDateFormatter::format),
                lockedDaysRemaining = lock?.daysRemaining ?: 0
            )
        }
    }

    private companion object {
        val lockDateFormatter: DateTimeFormatter = DateTimeFormatter
            .ofPattern("M/d/yy")
            .withLocale(Locale.US)
            .withZone(ZoneId.systemDefault())
    }
}
