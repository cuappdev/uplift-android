package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun DayOfWeekSelector(today: Int, onDaySelected: (Int) -> Unit) {
    var selectedDay by remember { mutableStateOf(today) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 46.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until 7) {
            Box(
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    selectedDay = i
                    onDaySelected(i)
                }
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = if (selectedDay == i) PRIMARY_YELLOW else if (today == i) GRAY01 else Color.Transparent
                ) {}
                Text(
                    text = when (i) {
                        0 -> "M"
                        1 -> "Tu"
                        2 -> "W"
                        3 -> "Th"
                        4 -> "Fr"
                        5 -> "Sa"
                        else -> "Su"
                    },
                    fontWeight = FontWeight(700),
                    fontSize = 12.sp,
                    lineHeight = 14.63.sp,
                    letterSpacing = 0.3.sp,
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    fontFamily = montserratFamily,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}