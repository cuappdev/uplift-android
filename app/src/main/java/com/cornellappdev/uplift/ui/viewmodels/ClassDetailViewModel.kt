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
     * A Stack containing all the previous classes seen, including the current class.
     */
    private val stack: Stack<UpliftClass> = Stack()

    private val _classFlow: MutableStateFlow<UpliftClass?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [UpliftClass] whose info should be displayed. Holds null if
     * there is no class selected (which shouldn't reasonably happen in any use case...)
     */
    val classFlow: StateFlow<UpliftClass?> = _classFlow.asStateFlow()

    /**
     * Sets the current class being displayed to [upliftClass].
     */
    fun openClass(upliftClass: UpliftClass) {
        stack.add(upliftClass)
        _classFlow.value = upliftClass
    }

    /**
     * Switches this ViewModel to display the previously queued class.
     */
    fun popBackStack() {
        // TODO: This causes a glitch-y appearance when popping from a class to another class. Research a fix.
        stack.pop()
        if (stack.isNotEmpty())
            _classFlow.value = stack.peek()
    }
}