package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.DayOfWeekBar
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch
import com.cornellappdev.uplift.ui.screens.reminders.Reminder
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.GRAY08
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

/**
 * @param reminder the reminder that is currently selected
 * @param selectedDays the days that are currently selected
 * @param onDaySelected callback for when the selected days change
 * @param checkedState whether the every day checkbox is checked
 * @param onCheckedChange callback for when the every day checkbox changes
 * @param setTimeOn whether the time picker is visible
 * @param onSetTimeChange callback for when the time picker visibility changes
 * @param hour the hour selected
 * @param minute the minute selected
 * @param isAm whether the time is AM or PM
 * @param onHourChange callback for when the hour changes
 * @param onMinuteChange callback for when the minute changes
 * @param onAmChange callback for when the AM/PM changes
 * @param addNewReminderState whether the user is currently adding a new reminder
 * @return component that displays the day selection, time picker, and every day checkbox
 * @sample EditReminderCard(
 *   reminder = Reminder("12:00 PM", setOf("M", "W", "F"), true),
 *   selectedDays = setOf("M", "W", "F"),
 *   onDaySelected = {},
 *   checkedState = true,
 *   onCheckedChange = {},
 *   setTimeOn = true,
 *   onSetTimeChange = {},
 *   hour = 12,
 *   minute = "00",
 *   isAm = false,
 *   onHourChange = {},
 *   onMinuteChange = {},
 *   onAmChange = {},
 *   addNewReminderState = false
 *   )
 */
@Composable
fun EditReminderCard(
    reminder: Reminder?,
    selectedDays: Set<String>,
    onDaySelected: (Set<String>) -> Unit,
    checkedState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    setTimeOn: Boolean,
    onSetTimeChange: (Boolean) -> Unit,
    hour: Int,
    minute: String,
    isAm: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (String) -> Unit,
    onAmChange: (Boolean) -> Unit,
    addNewReminderState: Boolean
) {

    val elevation by animateDpAsState(
        targetValue = if (addNewReminderState || reminder != null) 10.dp else 0.dp, label = String()
    )
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            DaySelection(selectedDays = selectedDays, onDaySelected = {
                onCheckedChange(false)
                onDaySelected(it)
            }, checkedState = checkedState, onCheckedChange = {
                onCheckedChange(it)
                val days = if (it) {
                    setOf("M", "T", "W", "Th", "F", "Sa", "Su")
                } else {
                    emptySet()
                }
                onDaySelected(days)
            })
            SetTimeRow(setTimeOn = setTimeOn, onSetTimeChange = onSetTimeChange)
            AnimatedVisibility(
                visible = setTimeOn, enter = expandVertically(), exit = shrinkVertically()
            ) {
                TimePicker(
                    hour = hour,
                    minute = minute,
                    isAm = isAm,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onAmChange = onAmChange
                )

            }
        }
    }
}

/**
 * @param selectedDays the days that are currently selected
 * @param onDaySelected callback for when the selected days change
 * @param checkedState whether the every day checkbox is checked
 * @param onCheckedChange callback for when the every day checkbox changes
 * @return component that displays the day selection and every day checkbox
 * @sample DaySelection(
 *  selectedDays = setOf("M", "W", "F"),
 *  onDaySelected = {},
 *  checkedState = true,
 *  onCheckedChange = {}
 *  )
 */
@Composable
private fun DaySelection(
    selectedDays: Set<String> = emptySet(),
    onDaySelected: (Set<String>) -> Unit,
    checkedState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DayOfWeekBar(
            selectedDays = selectedDays, isWorkoutReminder = true, onDaySelected = onDaySelected
        )
        InfoCheckboxRow(
            checkedState = checkedState, onCheckedChange = onCheckedChange
        )

    }
}

/**
 * @param hour the hour selected
 * @param minute the minute selected
 * @param isAm whether the time is AM or PM
 * @param onHourChange callback for when the hour changes
 * @param onMinuteChange callback for when the minute changes
 * @param onAmChange callback for when the AM/PM changes
 * @return TimePicker component that displays the hour, minute, and AM/PM selector
 * @sample TimePicker(
 * hour = 12,
 * minute = "00",
 * isAm = false,
 * onHourChange = {},
 * onMinuteChange = {},
 * onAmChange = {}
 * )
 */
@Composable
private fun TimePicker(
    hour: Int,
    minute: String,
    isAm: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (String) -> Unit,
    onAmChange: (Boolean) -> Unit
) {
    val hours = (1..12).toMutableList()
    val minutes = (0..55 step 5).map {
        it.toString().padStart(2, '0')
    }.toMutableList()
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
            20.dp, Alignment.CenterHorizontally
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScrollWheel(width = 70.dp,
                itemHeight = 40.dp,
                items = hours,
                initialItem = hour,
                textStyle = TextStyle(fontSize = 24.sp),
                textColor = GRAY08,
                selectedTextColor = Color.Black,
                onItemSelected = { i, _ ->
                    onHourChange(hours[i])
                })
            ScrollWheel(width = 70.dp,
                itemHeight = 40.dp,
                items = minutes,
                initialItem = minute,
                textStyle = TextStyle(fontSize = 24.sp),
                textColor = GRAY08,
                selectedTextColor = Color.Black,
                onItemSelected = { i, _ ->
                    onMinuteChange(minutes[i])
                })
        }
        AmPmSelector(
            initialIsAm = isAm, onValueChanged = onAmChange
        )

    }

}

/**
 * @param setTimeOn whether the time picker is visible
 * @param onSetTimeChange callback for when the time picker visibility changes
 * @return SetTimeRow component that displays the "Set Time" text, switch, and default time text
 * @sample SetTimeRow(
 * setTimeOn = true,
 * onSetTimeChange = {}
 * )
 */
@Composable
private fun SetTimeRow(
    setTimeOn: Boolean, onSetTimeChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set Time",
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            color = GRAY04,
            fontWeight = FontWeight.Bold
        )
        ReminderSwitch(checked = setTimeOn, onCheckedChange = onSetTimeChange)
        AnimatedVisibility(visible = !setTimeOn) {
            Text(
                text = "Default is 9:00 am",
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                color = GRAY04,
                fontWeight = FontWeight.Light
            )

        }

    }
}

/**
 * @param checkedState whether the every day checkbox is checked
 * @param onCheckedChange callback for when the every day checkbox changes
 * @return InfoCheckboxRow component that displays the every day checkbox and text
 * @sample InfoCheckboxRow(
 * checkedState = true,
 * onCheckedChange = {}
 * )
 */
@Composable
private fun InfoCheckboxRow(
    checkedState: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .height(24.dp)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = PRIMARY_YELLOW,
                checkmarkColor = Color.Black,
                uncheckedColor = GRAY04,
            ),
            modifier = Modifier.clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = "Every Day",
            fontSize = 14.sp,
            fontFamily = montserratFamily,
            color = GRAY04,
            fontWeight = FontWeight.SemiBold
        )
    }
}