package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import com.cornellappdev.uplift.data.models.gymdetail.Category
import com.cornellappdev.uplift.data.models.gymdetail.EquipmentInfo
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * GymEquipmentGroup is a composable that displays a grouping of gym equipment.
 * @param muscleGroupName the name of the muscle group
 * @param muscleImageId the image id of the muscle group
 * @param categories a list of categories of equipment (see [Category]) mainly for sub-muscle groups
 * @sample GymEquipmentGroupPreview
 */
@Composable
fun GymEquipmentGroup(
    muscleGroupName: String,
    muscleImageId: Int,
    categories: List<Category>
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
fun SubMuscleGroupDropdown(
    subMuscleGroupName: String,
    equipmentList: List<EquipmentInfo>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp),
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
 * EquipmentColumn is a composable that displays a column of equipment.
 * @param equipmentList a list of equipment info (see [EquipmentInfo])
 */
@Composable
fun EquipmentColumn(
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
                Text(
                    text = equipment.quantity.toString(),
                    modifier = Modifier.width(24.dp),
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
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
 * AnimatedVisibilityContent is a composable that animates the visibility of its content.
 * @param visible whether the content is visible
 * @param content the content to be displayed
 */
@Composable
fun AnimatedVisibilityContent(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
    ) {
        content()
    }
}

/**
 * ExpandableRow is a composable that displays a row that can be expanded.
 * @param text the text to be displayed
 * @param iconId the icon id to be displayed
 * @param expanded whether the row is expanded
 * @param onClick the action to be performed when the row is clicked
 * @param textSize the text size
 * @param fontWeight the font weight
 */
@Composable
fun ExpandableRow(
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
fun GymEquipmentGroupPreview() {
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
        Category("Chest", equipmentList1),
        Category("Back", equipmentList2)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GymEquipmentGroup("CHEST", R.drawable.ic_miscellaneous_equip, categories)
    }
}