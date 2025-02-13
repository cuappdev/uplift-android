package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.data.models.gymdetail.GymEquipmentGroupInfo
import com.cornellappdev.uplift.data.models.gymdetail.PopularTimes
import com.cornellappdev.uplift.data.models.gymdetail.TimeOfDay
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.todayIndex

/**
 * @param gym the gym to display
 * @param equipmentGroupInfoList the list of equipment group info to display (see [GymEquipmentGroupInfo])
 */
@Composable
fun FitnessCenterContent(
    gym: UpliftGym,
    equipmentGroupInfoList: List<GymEquipmentGroupInfo>
) {
    val day = todayIndex()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GymHours(
            gym.hours, day, isOpen(gym.hours[day])
        )
        SectionDivider()
        PopularTimesSection(
            popularTimes = PopularTimes(
                busyList = listOf(
                    20,
                    30,
                    40,
                    50,
                    50,
                    45,
                    35,
                    40,
                    50,
                    70,
                    80,
                    90,
                    95,
                    85,
                    70,
                    65,
                    20
                ),
                startTime = TimeOfDay(
                    hour = 6,
                    minute = 0,
                    isAM = true
                )
            )
        )
        SectionDivider()
        if (gym.amenities?.isNotEmpty() == true) {
            GymAmenitySection(gym.amenities)
            SectionDivider()
        }
        GymEquipmentSection(
            gymEquipmentGroupInfoList = equipmentGroupInfoList
        )
    }

}

@Composable
fun SectionDivider() {
    HorizontalDivider(
        color = GRAY01,
        thickness = 1.dp,
    )
}

