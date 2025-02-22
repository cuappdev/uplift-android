package com.cornellappdev.uplift.ui.viewmodels.gyms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
import com.cornellappdev.uplift.domain.gym.populartimes.GetPopularTimesUseCase
import com.cornellappdev.uplift.util.getSystemTime
import com.cornellappdev.uplift.util.sameDayAs
import com.cornellappdev.uplift.util.startTimeComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.GregorianCalendar
import java.util.Stack
import javax.inject.Inject

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
@HiltViewModel
class GymDetailViewModel @Inject constructor(
    private val upliftApiRepository: UpliftApiRepository,
    private val getPopularTimesUseCase: GetPopularTimesUseCase
) : ViewModel() {
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
        upliftApiRepository.classesApiFlow.combine(gymFlow) { apiResponse, gym ->
            when (apiResponse) {
                ApiResponse.Loading -> listOf()
                ApiResponse.Error -> listOf()
                is ApiResponse.Success -> apiResponse.data
                    .filter {
                        it.gymId == gym?.id
                                && it.date.sameDayAs(GregorianCalendar())
                                && it.time.end.compareTo(getSystemTime()) >= 0
                    }.sortedWith(startTimeComparator)

            }
        }.stateIn(
            CoroutineScope(Dispatchers.Main),
            SharingStarted.Eagerly,
            listOf()
        )

    private val _averageCapacitiesList = MutableStateFlow<List<Int>>(emptyList())
    val averageCapacitiesList = _averageCapacitiesList.asStateFlow()

    private fun setPopularTimes(facilityId: Int) {
        viewModelScope.launch {
            val currentDayOfWeek = LocalDateTime.now().dayOfWeek.value
            val popularTimes = getPopularTimesUseCase.execute(facilityId)
            val filteredPopularTimes = popularTimes
                .asSequence()
                .filter { it.dayOfWeek == currentDayOfWeek && it.hourOfDay in 6..23 }
                .sortedBy { it.hourOfDay }
                .map { it.averagePercent.toInt() }
                .toList()
            _averageCapacitiesList.value = filteredPopularTimes
        }
    }

    /**
     * Sets the current gym being displayed to [gym].
     */
    fun openGym(gym: UpliftGym) {
        stack.add(gym)
        _gymFlow.value = gym
        setPopularTimes(gym.facilityId.toInt())
    }

    /**
     * Switches this ViewModel to display the previously queued gym.
     */
    fun popBackStack() {
        stack.pop()
        if (stack.isNotEmpty())
            _gymFlow.value = stack.peek()
    }

    fun reload() {
        upliftApiRepository.reload()
    }
}
