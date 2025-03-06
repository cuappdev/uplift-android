package com.cornellappdev.uplift.ui.viewmodels.gyms

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.models.TimeOfDay
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTimesRepository
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
    val averageCapacities: List<Int>,
    val startTime: TimeOfDay = TimeOfDay(6, 0, true)
)

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
@HiltViewModel
class GymDetailViewModel @Inject constructor(
    private val upliftApiRepository: UpliftApiRepository,
    private val popularTimesRepository: PopularTimesRepository
) : UpliftViewModel<GymDetailUiState>(
    GymDetailUiState(
        todayClasses = emptyList(), averageCapacities = emptyList()
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
            it.gymId == gymId && it.date.sameDayAs(GregorianCalendar()) && it.time.end.compareTo(
                getSystemTime()
            ) >= 0
        }.sortedWith(startTimeComparator)
    }

    private fun setPopularTimes(facilityId: Int) {
        viewModelScope.launch {
            val gym = getStateValue().gym ?: return@launch
            val openHours = gym.hours
            val currentDayOfWeek = LocalDateTime.now().dayOfWeek.value
            val popularTimes = popularTimesRepository.getPopularTimes(facilityId)

            val todayOpenHours = openHours.getOrNull(currentDayOfWeek - 1)?.firstOrNull()
            val startHour =
                todayOpenHours?.start?.let { if (!it.isAM && it.hour != 12) it.hour + 12 else it.hour }
                    ?: 6
            val endHour = todayOpenHours?.end?.let {
                var hour = if (!it.isAM && it.hour != 12) it.hour + 12 else it.hour
                if (it.minute > 0) hour += 1
                hour
            } ?: 23

            val hourlyCapacities = MutableList(endHour - startHour + 1) { 0 }
            popularTimes.filter { it.dayOfWeek == currentDayOfWeek }.forEach { popularTime ->
                val hour = popularTime.hourOfDay
                if (hour in startHour..endHour) {
                    hourlyCapacities[hour - startHour] = popularTime.averagePercent.toInt()
                }
            }

            val startTime = TimeOfDay(startHour % 12, 0, startHour < 12)
            applyMutation { copy(averageCapacities = hourlyCapacities, startTime = startTime) }
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
        if (stack.isNotEmpty()) {
            applyMutation { copy(gym = stack.peek()) }
        }
        applyMutation {
            copy(averageCapacities = emptyList(), startTime = TimeOfDay(6, 0, true))
        }
    }

    fun reload() {
        upliftApiRepository.reload()
    }
}
