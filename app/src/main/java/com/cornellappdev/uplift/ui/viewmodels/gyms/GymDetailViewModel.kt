package com.cornellappdev.uplift.ui.viewmodels.gyms

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
import com.cornellappdev.uplift.domain.gym.populartimes.GetPopularTimesUseCase
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.getSystemTime
import com.cornellappdev.uplift.util.sameDayAs
import com.cornellappdev.uplift.util.startTimeComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.GregorianCalendar
import java.util.Stack
import javax.inject.Inject

data class GymDetailUiState(
    val gym: UpliftGym? = null,
    val todayClasses: List<UpliftClass>,
    val averageCapacities: List<Int>
)

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
@HiltViewModel
class GymDetailViewModel @Inject constructor(
    private val upliftApiRepository: UpliftApiRepository,
    private val getPopularTimesUseCase: GetPopularTimesUseCase
) : UpliftViewModel<GymDetailUiState>(
    GymDetailUiState(
        todayClasses = emptyList(),
        averageCapacities = emptyList()
    )
) {
    /**
     * A Stack containing all the previous gyms seen, including the current gym.
     */
    private val stack: Stack<UpliftGym> = Stack()

    init {
        viewModelScope.launch {
            upliftApiRepository.classesApiFlow.collect { apiResponse ->
                val gym = getStateValue().gym
                val todayClasses = when (apiResponse) {
                    ApiResponse.Loading, ApiResponse.Error -> emptyList()
                    is ApiResponse.Success -> filterAndSortClasses(apiResponse.data, gym?.id ?: "")
                }
                applyMutation { copy(todayClasses = todayClasses) }
            }
        }
    }

    private fun filterAndSortClasses(classes: List<UpliftClass>, gymId: String): List<UpliftClass> {
        return classes.filter {
            it.gymId == gymId
                    && it.date.sameDayAs(GregorianCalendar())
                    && it.time.end.compareTo(getSystemTime()) >= 0
        }.sortedWith(startTimeComparator)
    }

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
            applyMutation { copy(averageCapacities = filteredPopularTimes) }
        }
    }

    /**
     * Sets the current gym being displayed to [gym].
     */
    fun openGym(gym: UpliftGym) {
        stack.add(gym)
        applyMutation { copy(gym = gym) }
        setPopularTimes(gym.facilityId.toInt())
    }

    /**
     * Switches this ViewModel to display the previously queued gym.
     */
    fun popBackStack() {
        stack.pop()
        if (stack.isNotEmpty())
            applyMutation { copy(gym = stack.peek()) }
    }

    fun reload() {
        upliftApiRepository.reload()
    }
}
