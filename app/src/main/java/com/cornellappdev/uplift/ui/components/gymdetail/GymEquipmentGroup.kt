package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentCategory
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentInfo
import com.cornellappdev.uplift.ui.components.general.AnimatedVisibilityContent
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * GymEquipmentGroup is a composable that displays a grouping of gym equipment.
 * @param muscleGroupName the name of the muscle group
 * @param muscleImageId the image id of the muscle group
 * @param categories a list of categories of equipment (see [EquipmentCategory]) mainly for sub-muscle groups
 * @sample GymEquipmentGroupPreview
 */
@Composable
fun GymEquipmentGroup(
    muscleGroupName: String,
    @DrawableRes muscleImageId: Int,
    categories: List<EquipmentCategory>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ExpandableRow(
            text = muscleGroupName,
            iconId = muscleImageId,
            expanded = expanded,
            onClick = { expanded = !expanded }
        )

        AnimatedVisibilityContent(visible = expanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (categories.size == 1) {
                    categories[0].equipmentList?.let {
                        EquipmentColumn(it)
                    }
                } else {
                    categories.forEach { category ->
                        category.equipmentList?.let {
                            SubMuscleGroupDropdown(category.name, it)
                        }
                    }
                }

            }
        }
    }
}

/**
 * SubMuscleGroupDropdown is a composable that displays a dropdown of equipment for a sub-muscle group.
 * @param subMuscleGroupName the name of the sub-muscle group
 * @param equipmentList a list of equipment info for the sub-muscle group (see [EquipmentInfo])
 */
@Composable
private fun SubMuscleGroupDropdown(
    subMuscleGroupName: String,
    equipmentList: List<EquipmentInfo>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ExpandableRow(
            text = subMuscleGroupName,
            expanded = expanded,
            onClick = { expanded = !expanded },
            textSize = 14,
            fontWeight = FontWeight.Normal
        )

        AnimatedVisibilityContent(visible = expanded) {
            EquipmentColumn(equipmentList)
        }
    }
}

/**
 * EquipmentColumn is a composable that displays a list of quantity equipment pairs.
 * @param equipmentList a list of equipment info (see [EquipmentInfo])
 */
@Composable
private fun EquipmentColumn(
    equipmentList: List<EquipmentInfo>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        equipmentList.forEach { equipment ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (equipment.quantity > 0) {
                    Text(
                        text = equipment.quantity.toString(),
                        modifier = Modifier.width(24.dp),
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = equipment.name,
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * ExpandableRow is a composable used to display a dropdown for a muscle group or sub-muscle group.
 * @param text the text to be displayed
 * @param iconId the icon id to be displayed
 * @param expanded whether the row is expanded
 * @param onClick the action to be performed when the row is clicked
 * @param textSize the text size
 * @param fontWeight the font weight
 */
@Composable
private fun ExpandableRow(
    text: String,
    iconId: Int? = null,
    expanded: Boolean,
    onClick: () -> Unit,
    textSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Medium
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            iconId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = PRIMARY_BLACK
                )
            }
            Text(
                text = text,
                fontFamily = montserratFamily,
                fontSize = textSize.sp,
                fontWeight = fontWeight
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_caret_right),
            contentDescription = null,
            modifier = Modifier
                .size(9.dp)
                .rotate(if (expanded) 90f else 0f),
            tint = PRIMARY_BLACK
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GymEquipmentGroupPreview() {
    val equipmentList1 = listOf(
        EquipmentInfo("Bench", 10),
        EquipmentInfo("Dumbbells", 20),
        EquipmentInfo("Squat Rack", 5),
    )
    val equipmentList2 = listOf(
        EquipmentInfo("Bench", 10),
        EquipmentInfo("Dumbbells", 20),
        EquipmentInfo("Squat Rack", 5),
    )
    val categories = listOf(
        EquipmentCategory("Chest", equipmentList1),
        EquipmentCategory("Back", equipmentList2)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GymEquipmentGroup("CHEST", R.drawable.ic_miscellaneous_equip, categories)
    }
}