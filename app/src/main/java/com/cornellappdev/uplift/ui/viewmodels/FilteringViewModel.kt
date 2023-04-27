package com.cornellappdev.uplift.ui.viewmodels

import com.cornellappdev.uplift.models.UpliftClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilteringViewModel {
    private val _classesFlow: MutableStateFlow<List<UpliftClass?>> = MutableStateFlow(listOf(null))
    val classesFlow = _classesFlow.asStateFlow()
    fun instructorList(){
        //Get Instructors from API and collect as mutable state
    }

    fun clasTypesList(){
        //Get List of Classes from APi and collect as a mutable state
    }

}