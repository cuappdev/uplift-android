package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.models.UpliftActivity
import com.cornellappdev.uplift.models.UpliftGym
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Stack

/** A [ActivityDetailViewModel] is a view model for ActivityDetailScreen. */
class ActivityDetailViewModel : ViewModel() {
    /**
     * A Stack containing all the previous activities seen, including the current gym.
     */
    private val stack: Stack<UpliftActivity> = Stack()

    private val _activityFlow: MutableStateFlow<UpliftActivity?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [UpliftGym] whose info should be displayed. Holds null if
     * there is no gym selected (which shouldn't reasonably happen in any use case...)
     */
    val activityFlow: StateFlow<UpliftActivity?> = _activityFlow.asStateFlow()

//    /**
//     * A [Flow] detailing the [UpliftClass]es that should be displayed in the "Today's Classes" section.
//     */
//    val todaysClassesFlow =
//        UpliftApiRepository.classesApiFlow.combine(gymFlow) { apiResponse, gym ->
//            when (apiResponse) {
//                ApiResponse.Loading -> listOf()
//                ApiResponse.Error -> listOf()
//                is ApiResponse.Success -> apiResponse.data
//                    .filter {
//                        it.gymId == gym?.id
//                                && it.date.sameDayAs(GregorianCalendar())
//                                && it.time.end.compareTo(getSystemTime()) >= 0
//                    }
//            }
//        }.stateIn(
//            CoroutineScope(Dispatchers.Main),
//            SharingStarted.Eagerly,
//            listOf()
//        )

    /**
     * Sets the current activity being displayed to [activity].
     */
    fun openActivity(activity: UpliftActivity) {
        stack.add(activity)
        _activityFlow.value = activity
    }

    /**
     * Switches this ViewModel to display the previously queued activity.
     */
    fun popBackStack() {
        stack.pop()
        if (stack.isNotEmpty())
            _activityFlow.value = stack.peek()
    }
}

