package com.cornellappdev.uplift.ui.viewmodels.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.CheckInRepository
import com.cornellappdev.uplift.data.repositories.LocationRepository
import com.cornellappdev.uplift.data.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.todayIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CheckInMode {Prompt, Complete}

data class CheckInUiState(
    val showPopUp: Boolean = false,
    val mode: CheckInMode = CheckInMode.Prompt,
    val gymId: String? = "",
    val gymName: String = "",
    val timeText: String = "",
    val showConfetti: Boolean = false
)

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
) : UpliftViewModel<CheckInUiState>(CheckInUiState()) {

    private var locationJob: Job? = null
    private var hasCheckedInToday = false
    val day: Int = todayIndex()

    init {
        viewModelScope.launch {
            combine(
                checkInRepository.nearestGymFlow,
                checkInRepository.checkInPromptAllowed
            ) { gym, allowed -> Pair(gym, allowed) }
                .collect { (gym, allowed) ->
                    val currentMode = uiStateFlow.value.mode
                    val inComplete = currentMode == CheckInMode.Complete

                    when {
                         allowed && gym != null && !hasCheckedInToday && isOpen(gym.hours[day]) -> applyMutation {
                            copy(
                                showPopUp = true,
                                mode = if (inComplete) CheckInMode.Complete else CheckInMode.Prompt,
                                gymName = gym.name,
                                gymId = gym.id,
                                timeText = checkInRepository.formatTime(System.currentTimeMillis())
                            )
                        }
                        !inComplete -> applyMutation { copy(showPopUp = false) }
                    }

                }
        }
    }

    fun startLocationUpdates(context: Context) {
        if (locationJob?.isActive == true) return
        locationJob = viewModelScope.launch {
            try {
                LocationRepository.locationFlow(context).collect{ location ->
                   try {
                       if (location != null) {
                           checkInRepository.evaluateProximity(location)
                       }
                   } catch (e: Exception) {
                       Log.e("CheckInViewModel", "Error evaluating proximity from location update", e)
                   }
                }
            } catch (e: Exception) {
                Log.e("CheckInViewModel", "Error collecting location flow", e)
            }
        }
    }

    fun stopLocationUpdates() {
        locationJob?.cancel()
        locationJob = null
    }

    fun onDismiss() = viewModelScope.launch {
        try {
            checkInRepository.markCheckInDismissedFor()
        } catch (e: Exception) {
            Log.e("CheckInViewModel", "Error dismissing check-in", e)
        }
        onClose()
    }

    /**
     * Note: Temporarily skips over failed backend log workout call to keep functionality while auth and
     * sign in are not working.
     */
    fun onCheckIn() = viewModelScope.launch {
        val currentGymId = uiStateFlow.value.gymId
        val gymIdInt = currentGymId?.toIntOrNull()

        if (gymIdInt == null) {
            Log.w("CheckInVM", "Invalid or missing gym ID: $currentGymId")
            return@launch
        }
        try {
            hasCheckedInToday = true
            checkInRepository.markCheckInToday()
            applyMutation {
                copy(
                    showPopUp = true,
                    mode = CheckInMode.Complete
                )
            }
            val successful = checkInRepository.logWorkoutFromCheckIn(gymIdInt)
            if (successful) {
                Log.d("CheckInVM", "Workout logged successfully.")
            } else {
                Log.d("CheckInVM", "Workout logging skipped (no user ID).")
            }
        } catch (e: Exception) {
            Log.e("CheckInViewModel", "Error logging workout", e)
        }
    }

    fun onClose() {
        applyMutation { copy(showPopUp = false) }
    }
}