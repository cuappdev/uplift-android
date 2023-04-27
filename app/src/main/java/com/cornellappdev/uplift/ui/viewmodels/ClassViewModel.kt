package com.cornellappdev.uplift.ui.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ClassViewModel: ViewModel() {
    private val _classesFlow: MutableStateFlow<List<UpliftClass?>> = MutableStateFlow(listOf(null))

    val classesFlow = _classesFlow.asStateFlow()


    fun insructorChanges(instructors: List<String>){
        for (i in 0.. classesFlow.value.size-1){
            for( i in 0.. instructors.size-1) {
                if (classesFlow.value.get(i)?.instructorName?.equals(instructors.size) == true) {
                    break;
                }

            }
        }
    }

}