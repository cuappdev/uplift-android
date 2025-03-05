package com.cornellappdev.uplift.ui.viewmodels.classes

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.GregorianCalendar
import java.util.Stack
import javax.inject.Inject

/** A [ClassDetailViewModel] is a view model for ClassDetailScreen.*/
@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    private val upliftApiRepository: UpliftApiRepository
) : ViewModel() {
    /**
     * A Stack containing all the previous classes seen, including the current class.
     */
    private val stack: Stack<UpliftClass> = Stack()

    // TODO: Modify to use UIState instead of individual Flows
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
        upliftApiRepository.classesApiFlow.combine(classFlow) { apiResponse, upliftClass ->
            when (apiResponse) {
                ApiResponse.Loading -> listOf()
                ApiResponse.Error -> listOf()
                is ApiResponse.Success -> apiResponse.data.filter {
                    it.name == upliftClass?.name && it.date > GregorianCalendar()
                }.sortedWith { class1, class2 ->
                    class1.date.compareTo(class2.date)
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
