package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class ClassesViewModel : ViewModel() {
    private val _selectedDay: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedDay = _selectedDay.asStateFlow()

    /**
     * A flow emitting lists of classes for the classes page to show. Filters based on
     * [selectedDay], which is mutable by [selectDay].
     */
    val classesFlow: StateFlow<ApiResponse<List<UpliftClass>>> =
        UpliftApiRepository.classesApiFlow.combine(selectedDay) { apiResponse, day ->
            (when (apiResponse) {
                ApiResponse.Loading -> ApiResponse.Loading
                ApiResponse.Error -> ApiResponse.Error
                // Pull actual API response data, then filter those out by day.
                is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.filter {
                    val dateFromToday = Calendar.getInstance()
                    dateFromToday.add(Calendar.DAY_OF_YEAR, day)
                    it.date.get(Calendar.DAY_OF_YEAR) == dateFromToday.get(Calendar.DAY_OF_YEAR)
                }.sortedWith { class1, class2 ->
                    if (class1.time.start.compareTo(class2.time.start) != 0) {
                        class1.time.start.compareTo(class2.time.start)
                    } else {
                        class1.time.end.compareTo(class2.time.end)
                    }
                })
            })
        }.stateIn(
            CoroutineScope(Dispatchers.Main),
            SharingStarted.Eagerly,
            ApiResponse.Loading
        )

    /**
     * Selects a day in the future or past to display the classes of. A day of -x represents x days
     * ago, and a day of y represents y days into the future. 0 for today.
     */
    fun selectDay(day: Int) {
        _selectedDay.value = day
    }
}
