package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** A [ClassDetailViewModel] is a view model for ClassDetailScreen.*/
class ClassDetailViewModel : ViewModel() {
    private val _classFlow: MutableStateFlow<UpliftClass?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [UpliftClass] whose info should be displayed. Holds null if
     * there is no class selected (which shouldn't reasonably happen in any use case...)
     */
    val classFlow: StateFlow<UpliftClass?> = _classFlow.asStateFlow()

    fun selectClass(upliftClass: UpliftClass) {
        _classFlow.value = upliftClass
    }
}