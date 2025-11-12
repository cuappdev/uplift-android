package com.cornellappdev.uplift.ui.viewmodels.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.CheckInRepository
import com.cornellappdev.uplift.data.repositories.ConfettiRepository
import com.cornellappdev.uplift.data.repositories.LocationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.todayIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val tag = "CheckInVM"
/**
 * UI mode for the Check-In pop up.
 * - [Prompt]: user is near an open gym and can choose to check in.
 * -[Complete]: a check in was just logged and the confirmation/congratulation state is shown.
 */
enum class CheckInMode {Prompt, Complete}

/**
 * UI state backing the Check-In pop-up
 *
 * @param showPopUp whether the pop-up should be visible on screen
 * @param mode the current mode of the pop-up
 * @param gymId the identifier of the nearest open gym (null if unknown or not applicable)
 * @param gymName the display name of the nearest/open gym
 * @param timeText a pre-formatted time string to display
 * @param showConfetti whether a confetti animation should be shown after a successful check-in
 */
data class CheckInUiState(
    val showPopUp: Boolean = false,
    val mode: CheckInMode = CheckInMode.Prompt,
    val gymId: String? = "",
    val gymName: String = "",
    val timeText: String = ""
)

/** A [CheckInViewModel] manages state and actions for the Check-In pop-up on main screens. */
@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val confettiRepository: ConfettiRepository
) : UpliftViewModel<CheckInUiState>(CheckInUiState()) {

    private var locationJob: Job? = null
    private val day: Int = todayIndex()

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
                         allowed && gym != null && isOpen(gym.hours[day]) -> applyMutation {
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

    /**
     * Calls [LocationRepository.startLocationUpdates] to begin location updates and launches a
     * ViewModel scoped collector that feeds new non-null locations to the proximity evaluation flow.
     */
    fun startLocationUpdates(context: Context) {
        if (locationJob?.isActive == true) return
        LocationRepository.startLocationUpdates(context)
        locationJob = viewModelScope.launch {
            try {
                LocationRepository.currentLocationFlow.collect{ location ->
                   try {
                       if (location != null) {
                           checkInRepository.evaluateProximity(location)
                       }
                   } catch (e: Exception) {
                       Log.e(tag, "Error evaluating proximity from location update", e)
                   }
                }
            } catch (e: Exception) {
                Log.e(tag, "Error collecting location flow", e)
            }
        }
    }

    /**
     * Cancels location collection job and calls [LocationRepository.stopLocationUpdates] to stop
     * location updates.
     */
    fun stopLocationUpdates() {
        locationJob?.cancel()
        locationJob = null
        LocationRepository.stopLocationUpdates()
    }

    /**
     * Records a temporary cooldown window by calling [checkInRepository] to mark the user's dismissal
     * of the pop up.
     */
    fun onDismiss() = viewModelScope.launch {
        try {
            checkInRepository.markCheckInDismissedFor()
        } catch (e: Exception) {
            Log.e(tag, "Error dismissing check-in", e)
        }
        onClose()
    }

    /**
     * Marks the user as checked in for the day, triggering a cooldown til the end of day and a
     * logworkout mutation through [checkInRepository]. On a successful call, transitions UI into
     * [CheckInMode.Complete] and bursts confetti from popup through a [confettiRepository].
     *
     * Note: Temporarily skips over failed backend log workout call to keep functionality while auth and
     * sign in are not working.
     */
    fun onCheckIn() = viewModelScope.launch {
        val currentGymId = uiStateFlow.value.gymId
        val gymIdInt = currentGymId?.toIntOrNull()

        if (gymIdInt == null) {
            Log.w(tag, "Invalid or missing gym ID: $currentGymId")
            return@launch
        }
        try {
            checkInRepository.markCheckInToday()
            applyMutation {
                copy(
                    showPopUp = true,
                    mode = CheckInMode.Complete
                )
            }
            confettiRepository.showConfetti(ConfettiViewModel.ConfettiUiState())
            checkInRepository.logWorkoutFromCheckIn(gymIdInt)
        } catch (e: Exception) {
            Log.e(tag, "Error checking in", e)
        }
    }

    /**
     * Closes the pop-up immediately by not displaying the pop-up.
     */
    fun onClose() {
        applyMutation { copy(showPopUp = false) }
    }
}