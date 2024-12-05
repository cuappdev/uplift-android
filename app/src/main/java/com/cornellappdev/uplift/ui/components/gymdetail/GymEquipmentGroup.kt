package com.cornellappdev.uplift.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.EquipmentGrouping
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
                text = group.equipmentType.toString(),
                fontWeight = FontWeight(700),
                fontSize = 14.sp,
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Left,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            for (equipmentField in group.equipmentList) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = equipmentField.name,
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Left,
                        color = PRIMARY_BLACK,
                        fontFamily = montserratFamily
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    //TODO fix design once new design comes out
                    if (equipmentField.quantity != 0) {
                        Text(
                            text = equipmentField.quantity.toString(),
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
}
