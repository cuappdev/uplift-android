package com.cornellappdev.uplift.ui.components.capacityreminder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily


@Composable
fun GymTag(
    gymName: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .background(
                color = if (isSelected) LIGHT_YELLOW else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) PRIMARY_YELLOW else GRAY04,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSelected()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = gymName,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = GRAY04,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
