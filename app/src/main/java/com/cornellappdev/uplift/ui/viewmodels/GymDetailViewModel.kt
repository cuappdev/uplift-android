package com.cornellappdev.uplift.ui.viewmodels

import com.cornellappdev.uplift.models.Gym
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
class GymDetailViewModel {

    private val _gymFlow : MutableStateFlow<Gym?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [Gym] whose info should be displayed. Holds null if
     * there is no gym selected (which shouldn't reasonably happen in any use case...)
     */
    val gymFlow : StateFlow<Gym?> = _gymFlow.asStateFlow()

    fun selectGym(gym : Gym) {
        _gymFlow.value = gym
    }
}