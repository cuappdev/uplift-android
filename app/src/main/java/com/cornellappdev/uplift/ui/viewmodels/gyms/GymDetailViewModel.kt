package com.cornellappdev.uplift.ui.viewmodels.gyms


import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.mappers.toPopularTime
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.models.TimeOfDay
import com.cornellappdev.uplift.data.repositories.PopularTimesRepository
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
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
    val startTime: TimeOfDay = TimeOfDay(6, 0, true),
    val selectedPopularTimesIndex: Int = -1
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
            // TODO: Think about moving filtering logic to domain as business logic use case
            val currentDayOfWeek = LocalDateTime.now().dayOfWeek.value
            val popularTimes = popularTimesRepository.getPopularTimes(facilityId)
                .getOrNull()?.getHourlyAverageCapacitiesByFacilityId?.mapNotNull {
                    it?.toPopularTime()
                }

            val todayOpenHoursFirst = openHours.getOrNull(currentDayOfWeek - 1)?.firstOrNull()
            val todayOpenHoursLast = openHours.getOrNull(currentDayOfWeek - 1)?.lastOrNull()
            val startHour =
                todayOpenHoursFirst?.start?.let { if (!it.isAM && it.hour != 12) it.hour + 12 else it.hour }
                    ?: 6
            val endHour = todayOpenHoursLast?.end?.let {
                var hour = if (!it.isAM && it.hour != 12) it.hour + 12 else it.hour
                if (it.minute > 0) hour += 1
                hour
            } ?: 23

            val hourlyCapacities = popularTimes
                ?.filter { it.dayOfWeek == currentDayOfWeek }
                ?.associateBy { it.hourOfDay }
                .let { popularTimesMap ->
                    (startHour..endHour).map { hour ->
                        popularTimesMap?.get(hour)?.averagePercent?.toInt() ?: 0
                    }
                }

            val startTime = TimeOfDay(startHour % 12, 0, startHour < 12)
            val selectedPopularTimesIndex =
                calculateCurrentTimeIndex(startTime, hourlyCapacities.size)
            applyMutation {
                copy(
                    averageCapacities = hourlyCapacities,
                    startTime = startTime,
                    selectedPopularTimesIndex = selectedPopularTimesIndex
                )
            }
        }
    }

    private fun calculateCurrentTimeIndex(startTime: TimeOfDay, capacitiesSize: Int): Int {
        val currentTime = java.util.Calendar.getInstance()
        val currentHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(java.util.Calendar.MINUTE)

        val startHour = if (startTime.isAM) startTime.hour else startTime.hour + 12
        val totalStartMinutes = startHour * 60 + startTime.minute
        val totalCurrentMinutes = currentHour * 60 + currentMinute
        val index = (totalCurrentMinutes - totalStartMinutes) / 60

        return if (index in 0 until capacitiesSize) index else -1
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
        var gym = getStateValue().gym
        if (stack.isNotEmpty()) {
            gym = stack.peek()
        }
        applyMutation {
            copy(gym = gym, averageCapacities = emptyList(), startTime = TimeOfDay(6, 0, true))
        }
    }

    fun reload() {
        upliftApiRepository.reload()
    }
}
