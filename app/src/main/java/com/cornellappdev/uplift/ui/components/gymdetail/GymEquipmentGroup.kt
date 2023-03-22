package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.EquipmentGrouping
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A bubble displaying information about an [EquipmentGrouping].
 */
@Composable
fun GymEquipmentGroup(group: EquipmentGrouping) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(247.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = group.name,
                fontWeight = FontWeight(700),
                fontSize = 14.sp,
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Left,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            for ((name, num) in group.equipmentList) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Left,
                        color = PRIMARY_BLACK,
                        fontFamily = montserratFamily
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = num.toString(),
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Left,
                        color = PRIMARY_BLACK,
                        fontFamily = montserratFamily
                    )
                }
            }
        }
    }
}