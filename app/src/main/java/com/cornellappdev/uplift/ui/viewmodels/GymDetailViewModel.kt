package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.models.gymdetail.Category
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentGrouping
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentInfo
import com.cornellappdev.uplift.data.models.gymdetail.GymEquipmentGroupInfo
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
import com.cornellappdev.uplift.type.MuscleGroup
import com.cornellappdev.uplift.util.getSystemTime
import com.cornellappdev.uplift.util.gymdetail.majorMuscleToImageId
import com.cornellappdev.uplift.util.gymdetail.majorToSubGroupMap
import com.cornellappdev.uplift.util.sameDayAs
import com.cornellappdev.uplift.util.startTimeComparator
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

/** A [GymDetailViewModel] is a view model for GymDetailScreen. */
@HiltViewModel
class GymDetailViewModel @Inject constructor(
    private val upliftApiRepository: UpliftApiRepository
) : ViewModel() {
    /**
     * A Stack containing all the previous gyms seen, including the current gym.
     */
    private val stack: Stack<UpliftGym> = Stack()

    private val _gymFlow: MutableStateFlow<UpliftGym?> = MutableStateFlow(null)

    /**
     * A [StateFlow] detailing the [UpliftGym] whose info should be displayed. Holds null if
     * there is no gym selected (which shouldn't reasonably happen in any use case...)
     */
    val gymFlow: StateFlow<UpliftGym?> = _gymFlow.asStateFlow()

    /**
     * A [Flow] detailing the [UpliftClass]es that should be displayed in the "Today's Classes" section.
     */
    val todaysClassesFlow =
        upliftApiRepository.classesApiFlow.combine(gymFlow) { apiResponse, gym ->
            when (apiResponse) {
                ApiResponse.Loading -> listOf()
                ApiResponse.Error -> listOf()
                is ApiResponse.Success -> apiResponse.data
                    .filter {
                        it.gymId == gym?.id
                                && it.date.sameDayAs(GregorianCalendar())
                                && it.time.end.compareTo(getSystemTime()) >= 0
                    }.sortedWith(startTimeComparator)

            }
        }.stateIn(
            CoroutineScope(Dispatchers.Main),
            SharingStarted.Eagerly,
            listOf()
        )

    /**
     * Returns a list of [GymEquipmentGroupInfo]s based on the [equipmentGroupingMap].
     * @param equipmentGroupingMap a map of [MuscleGroup] to [EquipmentGrouping]
     * @return a list of [GymEquipmentGroupInfo]s
     */
    fun getEquipmentGroupInfoList(
        equipmentGroupingMap: HashMap<MuscleGroup, EquipmentGrouping>,
    ): List<GymEquipmentGroupInfo> {
        return majorToSubGroupMap.map { (majorGroup, subGroups) ->
            val categories = subGroups.map { subGroup ->
                val equipmentList =
                    equipmentGroupingMap[MuscleGroup.valueOf(subGroup.uppercase())]?.equipmentList
                val equipmentInfoList =
                    equipmentList?.map { EquipmentInfo(it.name, it.quantity) } ?: emptyList()
                Category(subGroup, equipmentInfoList)
            }
            GymEquipmentGroupInfo(
                majorGroup,
                majorMuscleToImageId[majorGroup]!!,
                categories
            )
        }
    }


    /**
     * Sets the current gym being displayed to [gym].
     */
    fun openGym(gym: UpliftGym) {
        stack.add(gym)
        _gymFlow.value = gym
    }

    /**
     * Switches this ViewModel to display the previously queued gym.
     */
    fun popBackStack() {
        stack.pop()
        if (stack.isNotEmpty())
            _gymFlow.value = stack.peek()
    }

    fun reload() {
        upliftApiRepository.reload()
    }
}
