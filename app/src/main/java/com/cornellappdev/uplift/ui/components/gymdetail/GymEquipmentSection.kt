package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.gymdetail.GymEquipmentGroupInfo
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * GymEquipmentSection is a composable that displays the equipment section of a gym.
 * @param gymEquipmentGroupInfoList the list of gym equipment group info to display
 */
@Composable
fun GymEquipmentSection(
    gymEquipmentGroupInfoList: List<GymEquipmentGroupInfo>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(
            text = "Equipment",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PRIMARY_BLACK
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ){
            gymEquipmentGroupInfoList.forEach { gymEquipmentGroupInfo ->
                GymEquipmentGroup(
                    muscleGroupName = gymEquipmentGroupInfo.muscleGroupName,
                    muscleImageId = gymEquipmentGroupInfo.muscleImageId,
                    categories = gymEquipmentGroupInfo.categories
                )
                if (gymEquipmentGroupInfo != gymEquipmentGroupInfoList.last()) {
                    SectionDivider()
                }
            }

        }

    }
}
