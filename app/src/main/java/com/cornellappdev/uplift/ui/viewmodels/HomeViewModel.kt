package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.Sport
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** A [HomeViewModel] is a view model for HomeScreen. */
class HomeViewModel : ViewModel() {

    private val _titleFlow: MutableStateFlow<String> = MutableStateFlow(getHomeTitleText())

    /** Emits values containing the title text the home page should display. */
    val titleFlow = _titleFlow.asStateFlow()

    private val _classesFlow: MutableStateFlow<List<UpliftClass>> = MutableStateFlow(
        listOf(
            exampleClassMusclePump1,
            exampleClassMusclePump2,
            exampleClassMusclePump3,
            exampleClassMusclePump4,
            exampleClassMusclePump5,
            exampleClassMusclePump6
        )
    )

    /** Emits lists of all the [UpliftClass]es that should be shown in the today's classes section. */
    val classesFlow = _classesFlow.asStateFlow()

    private val _sportsFlow: MutableStateFlow<List<Sport>> = MutableStateFlow(sports)

    /** Emits lists of sports that should be shown in the 'Your Sports' section. */
    val sportsFlow = _sportsFlow.asStateFlow()

    private val _gymFlow: MutableStateFlow<List<UpliftGym>> = MutableStateFlow(
        listOf(
            testMorrison, testMorrison
        )
    )

    /** Emits lists of gyms that should be shown in the 'Gyms' section. */
    val gymFlow = _gymFlow.asStateFlow()

    /** Call before opening home to set all the proper display information for the home page. */
    fun openHome() {
        _titleFlow.value = getHomeTitleText()
    }

    /**
     * Sets the [UpliftGym]s that this ViewModel should display.
     */
    fun emitGyms(gyms: List<UpliftGym>) {
        _gymFlow.value = gyms
    }

    /** Returns the title text the top bar should display for the home page. */
    private fun getHomeTitleText(): String {
        val now = getSystemTime()
        return if (now.compareTo(TimeOfDay(4, 0, true)) >= 0 &&
            now.compareTo(TimeOfDay(12, 0, false)) < 0
        )
            "Good Morning!"
        // 12 PM to 6 PM
        else if (now.compareTo(TimeOfDay(12, 0, false)) >= 0 && now.compareTo(
                TimeOfDay(6, 0, false)
            ) < 0
        )
            "Good Afternoon!"
        // 6 PM to 4 AM
        else "Good Evening!"
    }
}