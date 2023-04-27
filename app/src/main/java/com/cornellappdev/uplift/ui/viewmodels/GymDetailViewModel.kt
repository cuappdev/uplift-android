package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.networking.toUpliftClass
import com.cornellappdev.uplift.util.getSystemTime
import com.cornellappdev.uplift.util.sameDayAs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
class GymDetailViewModel : ViewModel() {
    /**
     * A Stack containing all the previous gyms seen, including the current gym.
     */
    private val stack: Stack<UpliftGym> = Stack()

    private val _gymFlow: MutableStateFlow<UpliftGym?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [UpliftGym] whose info should be displayed. Holds null if
     * there is no gym selected (which shouldn't reasonably happen in any use case...)
     */
    val gymFlow: StateFlow<UpliftGym?> = _gymFlow.asStateFlow()

    /**
     * A [Flow] detailing the [UpliftClass]es that should be displayed in the "Today's Classes" section.
     */
    val todaysClassesFlow =
        UpliftApiRepository.classesApiFlow.combine(gymFlow) { apiResponse, gym ->
            when (apiResponse) {
                ApiResponse.Loading -> listOf()
                ApiResponse.Error -> listOf()
                is ApiResponse.Success -> apiResponse.data.map { query ->
                    query.toUpliftClass()
                }.filter {
                    it.gymId == gym?.id
                            && it.date.sameDayAs(GregorianCalendar())
                            && it.time.end.compareTo(getSystemTime()) >= 0
                }
            }
        }.stateIn(
            CoroutineScope(Dispatchers.Main),
            SharingStarted.Eagerly,
            listOf()
        )

    /**
     * Sets the current gym being displayed to [gym].
     */
    fun openGym(gym: UpliftGym) {
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