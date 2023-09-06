package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.networking.toUpliftClass
import com.cornellappdev.uplift.util.exampleClassMusclePump1
import com.cornellappdev.uplift.util.exampleClassMusclePump2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class ClassesViewModel : ViewModel() {

    private val _selectedDay: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedDay = _selectedDay.asStateFlow()

    // Testing for now to demonstrate filtering. Classes are hardcoded on Sept 6th and 7th.
    private val example = listOf(
        exampleClassMusclePump1,
        exampleClassMusclePump2,
        exampleClassMusclePump1,
        exampleClassMusclePump2,
        exampleClassMusclePump1,
        exampleClassMusclePump2
    )

    /**
     * A flow emitting lists of classes for the classes page to show. Filters based on
     * [selectedDay], which is mutable by [selectDay].
     */
    val classesFlow: StateFlow<ApiResponse<List<UpliftClass>>> =
        UpliftApiRepository.classesApiFlow.combine(selectedDay) { apiResponse, day ->
            (when (apiResponse) {
                // TODO: Replace w/ simply `ApiResponse.Loading` when backend up.
                ApiResponse.Loading -> ApiResponse.Success(example.filter {
                    val dateFromToday = Calendar.getInstance()
                    dateFromToday.add(Calendar.DAY_OF_YEAR, day)
                    it.date.get(Calendar.DAY_OF_YEAR) == dateFromToday.get(Calendar.DAY_OF_YEAR)
                })
                // TODO: Replace w/ simply `ApiResponse.Error` when backend up.
                ApiResponse.Error -> ApiResponse.Success(example.filter {
                    val dateFromToday = Calendar.getInstance()
                    dateFromToday.add(Calendar.DAY_OF_YEAR, day)
                    it.date.get(Calendar.DAY_OF_YEAR) == dateFromToday.get(Calendar.DAY_OF_YEAR)
                })
                // Pull actual API response data, then filter those out by day.
                is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.map { query ->
                    query.toUpliftClass()
                }.filter {
                    val dateFromToday = Calendar.getInstance()
                    dateFromToday.add(Calendar.DAY_OF_YEAR, day)
                    it.date.get(Calendar.DAY_OF_YEAR) == dateFromToday.get(Calendar.DAY_OF_YEAR)
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
