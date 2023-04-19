package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.Gym
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
class GymDetailViewModel : ViewModel() {
    /**
     * A Stack containing all the previous gyms seen, including the current gym.
     */
    private val stack: Stack<Gym> = Stack()

    private val _gymFlow: MutableStateFlow<Gym?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [Gym] whose info should be displayed. Holds null if
     * there is no gym selected (which shouldn't reasonably happen in any use case...)
     */
    val gymFlow: StateFlow<Gym?> = _gymFlow.asStateFlow()

    /**
     * Sets the current gym being displayed to [gym].
     */
    fun openGym(gym: Gym) {
        stack.add(gym)
        _gymFlow.value = gym
    }

    /**
     * Switches this ViewModel to display the previously queued gym.
     */
    fun popBackStack() {
        stack.pop()
        if (stack.isNotEmpty())
            _gymFlow.value = stack.peek()
    }
}