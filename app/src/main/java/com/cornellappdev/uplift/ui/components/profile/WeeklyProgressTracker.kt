package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun WeeklyProgressTracker(
    daysOfMonth: List<Int>,
    completedDays: List<Boolean>
) {
    val daysOfWeek = listOf("M", "T", "W", "Th", "F", "Sa", "Su")
    val lastCompletedIndex = completedDays.indexOfLast { it }

    Box(modifier = Modifier.fillMaxWidth()) {
        ConnectingLines(daysOfWeek, lastCompletedIndex)
        DayProgressCirclesRow(daysOfWeek, completedDays, daysOfMonth)
    }
}

@Composable
private fun DayProgressCirclesRow(
    daysOfWeek: List<String>,
    completedDays: List<Boolean>,
    daysOfMonth: List<Int>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEachIndexed { index, dayOfWeek ->
            DayProgressCircle(
                dayOfWeek = dayOfWeek,
                isCompleted = completedDays[index],
                day = daysOfMonth[index]
            )
        }
    }
}

@Composable
private fun BoxScope.ConnectingLines(
    daysOfWeek: List<String>,
    lastCompletedIndex: Int
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .padding(horizontal = 14.dp)
    ) {
        val itemWidth = size.width / daysOfWeek.size
        val circleRadius = 12.dp.toPx() // Half of the circle size (24.dp)
        val lineY = size.height / 2

        for (i in 0 until daysOfWeek.size - 1) {
            val startX = (i * itemWidth) + itemWidth / 2 + circleRadius
            val endX = ((i + 1) * itemWidth) + itemWidth / 2 - circleRadius
            val isSolid = i <= lastCompletedIndex && i + 1 <= lastCompletedIndex

            if (isSolid) {
                // Draw solid line
                drawLine(
                    color = GRAY01,
                    start = Offset(startX, lineY),
                    end = Offset(endX, lineY),
                    strokeWidth = 2.5.dp.toPx()
                )
            } else {
                // Draw dotted line
                drawLine(
                    color = GRAY01,
                    start = Offset(startX, lineY),
                    end = Offset(endX, lineY),
                    strokeWidth = 2.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                )
            }
        }
    }
}

@Composable
private fun DayProgressCircle(
    dayOfWeek: String,
    isCompleted: Boolean,
    day: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = dayOfWeek,
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        ProgressCircle(isCompleted)
        Text(
            text = day.toString(),
            fontFamily = montserratFamily,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ProgressCircle(isCompleted: Boolean) {
    if (isCompleted) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = LIGHT_YELLOW
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(LIGHT_YELLOW, PRIMARY_YELLOW)
                    ),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checkmark",
                modifier = Modifier
                    .width(20.dp)
                    .height(14.dp)
            )
        }
    } else {
        Surface(
            color = GRAY01,
            shape = CircleShape,
            modifier = Modifier
                .size(24.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = GRAY02
                )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyProgressTrackerPreview() {
    val daysOfMonth = (25..31).toList()
    val completedDays = listOf(true, false, true, false, true, false, false)
    WeeklyProgressTracker(
        daysOfMonth,
        completedDays
    )
}