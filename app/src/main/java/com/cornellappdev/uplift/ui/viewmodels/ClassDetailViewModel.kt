package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

/** A [ClassDetailViewModel] is a view model for ClassDetailScreen.*/
class ClassDetailViewModel : ViewModel() {
    /**
     * A Stack containing all the previous classes seen, not including the current class.
     */
    private val stack: Stack<UpliftClass> = Stack()

    private val _classFlow: MutableStateFlow<UpliftClass?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [UpliftClass] whose info should be displayed. Holds null if
     * there is no class selected (which shouldn't reasonably happen in any use case...)
     */
    val classFlow: StateFlow<UpliftClass?> = _classFlow.asStateFlow()

    /**
     * Queues a new class to be opened when the class screen is next navigated to.
     */
    fun selectClass(upliftClass: UpliftClass) {
        stack.add(upliftClass)
    }

    /**
     * Switches this ViewModel to display the most recently queued gym.
     */
    fun popBackStack() {
        // TODO: This causes a glitch-y appearance when popping from a class to another class. Research a fix.
        _classFlow.value = stack.pop()
    }
}