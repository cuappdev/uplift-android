package com.cornellappdev.uplift.ui.components.general

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.calendarDayOfWeekToString
import com.cornellappdev.uplift.util.montserratFamily
import java.util.Calendar
import kotlin.math.roundToInt

/**
 * A scrollable row of calendar days starting from 3 days before the current day, going [daysAhead]
 * days into the future. Calls [onDaySelected] with the day in the future selected.
 *
 * For example, if tomorrow is selected, 1 is passed to the function call. If yesterday, -1 is
 * passed.
 */
@Composable
fun CalendarBar(
    selectedDay: Int, daysAhead: Int = 14,
    onDaySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        for (i in -3 until daysAhead + 1) {
            val day = Calendar.getInstance()
            day.add(Calendar.DAY_OF_YEAR, i)
            CalendarBarSelection(selected = selectedDay == i, day = day) {
                onDaySelected(i)
            }
        }
    }
}

/**
 * A scrollable row of days of the week starting from the current day. Calls [onDaySelected] with
 * the day of the week selected.
 */
@Composable
fun DayOfWeekBar(
    selectedDays: Set<String> = emptySet(),
    onDaySelected: (Set<String>) -> Unit
) {
    val daysOfWeek = listOf("M", "T", "W", "Th", "F", "Sa", "Su")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        daysOfWeek.forEachIndexed { _, day ->
            DayOfWeekSelection(
                day = day,
                selected = selectedDays.contains(day),
                onSelect = {
                    val newSelectedDays = if (selectedDays.contains(day)) {
                        selectedDays.minus(day)
                    } else {
                        selectedDays.plus(day)
                    }
                    onDaySelected(newSelectedDays)
                })
        }
    }
}


/**
 * A single selection of a calendar bar that shows the day of the week, day of the month,
 * and a circle indicating if the day is selected or not.
 */
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun CalendarBarSelection(
    day: Calendar,
    bubble: Boolean = false,
    selected: Boolean = false,
    onSelect: () -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val size = animateFloatAsState(if (selected) 24f else 0f)
    val fontWeight = animateFloatAsState(if (selected) 700f else 500f)

    Column(
        modifier = Modifier
            .height(58.dp)
            .width(screenWidth / 7f)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onSelect
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = if (bubble) PRIMARY_YELLOW else Color.Transparent,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .size(width = 9.dp, height = 9.dp)
        ) {}
        Text(
            text = calendarDayOfWeekToString(day, abbreviated = true),
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight(fontWeight.value.roundToInt()),
            color = if (selected) PRIMARY_BLACK else GRAY02,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
        )

        Box(modifier = Modifier.size(24.dp)) {
            if (day.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                Surface(
                    shape = CircleShape,
                    color = GRAY01,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                ) {}
            }
            Surface(
                shape = CircleShape,
                color = if (selected) PRIMARY_YELLOW
                else Color.Transparent,
                modifier = Modifier
                    .size(size.value.dp)
                    .align(Alignment.Center)
            ) {}
            Text(
                text = day.get(Calendar.DAY_OF_MONTH).toString(),
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight(fontWeight.value.roundToInt()),
                color = if (selected) PRIMARY_BLACK else GRAY02,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

/**
 * A single selection of a day of week bar that shows the day of the week,
 * and a circle indicating if the day of week is selected or not.
 */
@Composable
fun DayOfWeekSelection(
    day: String,
    selected: Boolean = false,
    onSelect: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val size = animateFloatAsState(if (selected) 24f else 0f, label = "size")
    Column(
        modifier = Modifier
            .height(58.dp)
            .width(screenWidth / 10f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(24.dp)) {
            Surface(
                shape = CircleShape,
                color = if (selected) PRIMARY_YELLOW
                else Color.Transparent,
                modifier = Modifier
                    .size(size.value.dp)
                    .align(Alignment.Center)
            ) {}
            Text(
                text = day,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PRIMARY_BLACK,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun CalendarBarPreview() {
    var selectedDays by remember { mutableStateOf(setOf("Sa", "M")) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        DayOfWeekBar(selectedDays = selectedDays) {
            selectedDays = it
        }
    }
}
