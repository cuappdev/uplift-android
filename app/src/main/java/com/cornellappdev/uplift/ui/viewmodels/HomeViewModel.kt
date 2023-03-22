package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.util.exampleClassMusclePump1
import com.cornellappdev.uplift.util.exampleClassMusclePump2
import com.cornellappdev.uplift.util.getSystemTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** A [HomeViewModel] is a view model for HomeScreen. */
class HomeViewModel : ViewModel() {

    private val _titleFlow: MutableStateFlow<String> = MutableStateFlow(getHomeTitleText())

    /** Emits values containing the title text the home page should display. */
    val titleFlow = _titleFlow.asStateFlow()

    private val _classesFlow: MutableStateFlow<List<UpliftClass>> = MutableStateFlow(
        listOf(
            exampleClassMusclePump1, exampleClassMusclePump2
        )
    )

    /** Emits lists of all the [UpliftClass]es that should be shown in the today's classes section. */
    val classesFlow = _classesFlow.asStateFlow()

    /** Call before opening home to set all the proper display information for the home page. */
    fun openHome() {
        _titleFlow.value = getHomeTitleText()
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