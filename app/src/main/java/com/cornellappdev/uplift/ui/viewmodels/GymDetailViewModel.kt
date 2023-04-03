package com.cornellappdev.uplift.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.Gym
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
class GymDetailViewModel : ViewModel() {
    /**
     * A Stack containing all the previous gyms seen, not including the current gym.
     */
    private val stack: Stack<Gym> = Stack()

    private val _gymFlow: MutableStateFlow<Gym?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [Gym] whose info should be displayed. Holds null if
     * there is no gym selected (which shouldn't reasonably happen in any use case...)
     */
    val gymFlow: StateFlow<Gym?> = _gymFlow.asStateFlow()

    /**
     * Queues a new gym to be opened when the gym screen is next navigated to.
     */
    fun selectGym(gym: Gym) {
        stack.add(gym)
    }

    /**
     * Switches this ViewModel to display the most recently queued gym.
     */
    fun popBackStack() {
        _gymFlow.value = stack.pop()
        Log.d("navtest", stack.size.toString())
    }
}