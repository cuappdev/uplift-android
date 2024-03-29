package com.cornellappdev.uplift.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.util.getSystemTime
import com.cornellappdev.uplift.util.sameDayAs
import com.cornellappdev.uplift.util.startTimeComparator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.GregorianCalendar

/** A [HomeViewModel] is a view model for HomeScreen. */
class HomeViewModel : ViewModel() {

    private val _titleFlow: MutableStateFlow<String> = MutableStateFlow(getHomeTitleText())

    /** Emits values containing the title text the home page should display. */
    val titleFlow = _titleFlow.asStateFlow()

    /** Emits lists of all the [UpliftClass]es that should be shown in the today's classes section. */
    val classesFlow = UpliftApiRepository.classesApiFlow.map { apiResponse ->
        when (apiResponse) {
            ApiResponse.Loading -> ApiResponse.Loading
            ApiResponse.Error -> ApiResponse.Error
            is ApiResponse.Success -> ApiResponse.Success(apiResponse.data
                .filter { upliftClass ->
                    upliftClass.date.sameDayAs(GregorianCalendar())
                }.filter { upliftClass ->
                    upliftClass.time.end.compareTo(getSystemTime()) > 0
                }.sortedWith(startTimeComparator)
            )
        }
    }.stateIn(
        CoroutineScope(Dispatchers.Main), SharingStarted.Eagerly, ApiResponse.Loading
    )

    /** Emits lists of gyms that should be shown in the 'Gyms' section. */
    val gymFlow = UpliftApiRepository.gymApiFlow.map { apiResponse ->
        when (apiResponse) {
            ApiResponse.Loading -> ApiResponse.Loading
            ApiResponse.Error -> ApiResponse.Error
            is ApiResponse.Success -> ApiResponse.Success(apiResponse.data)
        }
    }.stateIn(
        CoroutineScope(Dispatchers.Main), SharingStarted.Eagerly, ApiResponse.Loading
    )

    /**
     * Whether the home screen should show capacities.
     */
    val showCapacities: State<Boolean> = mutableStateOf(false)

    /** Toggles whether the capacities window should be shown. */
    fun toggleCapacities() {
        (showCapacities as MutableState).value = !showCapacities.value
    }

    /** Call before opening home to set all the proper display information for the home page. */
    fun openHome() {
        _titleFlow.value = getHomeTitleText()
    }

    /** Returns the title text the top bar should display for the home page. */
    private fun getHomeTitleText(): String {
        val now = getSystemTime()
        return if (now.compareTo(TimeOfDay(4, 0, true)) >= 0 && now.compareTo(
                TimeOfDay(12, 0, false)
            ) < 0
        ) "Good Morning!"
        // 12 PM to 6 PM
        else if (now.compareTo(TimeOfDay(12, 0, false)) >= 0 && now.compareTo(
                TimeOfDay(6, 0, false)
            ) < 0
        ) "Good Afternoon!"
        // 6 PM to 4 AM
        else "Good Evening!"
    }
}
