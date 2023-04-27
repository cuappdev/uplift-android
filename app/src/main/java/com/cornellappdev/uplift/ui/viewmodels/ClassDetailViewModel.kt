package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.networking.toUpliftClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
     * A [Flow] detailing the [UpliftClass]es to display in the Next Sessions section.
     */
    val nextSessionsFlow =
        UpliftApiRepository.classesApiFlow.combine(classFlow) { apiResponse, upliftClass ->
            when (apiResponse) {
                ApiResponse.Loading -> listOf()
                ApiResponse.Error -> listOf()
                is ApiResponse.Success -> apiResponse.data.map { query ->
                    query.toUpliftClass()
                }.filter {
                    it.name == upliftClass?.name && it.date > GregorianCalendar()
                }
            }
        }.stateIn(
            CoroutineScope(Dispatchers.Main),
            SharingStarted.Eagerly,
            listOf()
        )

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