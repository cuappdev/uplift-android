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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.DayOfWeekBar
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch
import com.cornellappdev.uplift.ui.screens.Reminder
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.GRAY08
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun EditReminderSection(
    addNewReminderState: Boolean = false,
    onAddNewReminderStateChange: (Boolean) -> Unit = {},
    reminders: List<Reminder> = emptyList(),
    onRemindersChange: (List<Reminder>) -> Unit = {}
) {
    var selectedDays by remember { mutableStateOf(setOf("W")) }
    var checkedState by remember { mutableStateOf(false) }
    var setTimeOn by remember { mutableStateOf(false) }
    var hour by remember { mutableStateOf(9) }
    var minute by remember { mutableStateOf("00") }
    var isAm by remember { mutableStateOf(true) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditReminderCard(
            selectedDays = selectedDays,
            onDaySelected = { selectedDays = it },
            checkedState = checkedState,
            onCheckedChange = { checkedState = it },
            setTimeOn = setTimeOn,
            onSetTimeChange = { setTimeOn = it },
            hour = hour,
            minute = minute,
            isAm = isAm,
            onHourChange = { hour = it },
            onMinuteChange = { minute = it },
            onAmChange = { isAm = it },
            addNewReminderState = addNewReminderState
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                8.dp, Alignment.CenterHorizontally
            )
        ) {
            DeleteButton(onClick = {})
            SubmitReminderButton(onClick = {
                val newReminder = Reminder(time = "$hour:$minute ${if (isAm) "AM" else "PM"}",
                    days = selectedDays,
                    enabled = true,
                    onEnabledChange = {})
                onRemindersChange(listOf(newReminder) + reminders)
                onAddNewReminderStateChange(false)

            })

        }
    }
}

@Composable
fun EditReminderCard(
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
    addNewReminderState: Boolean = false
) {

    val elevation by animateDpAsState(
        targetValue = if (addNewReminderState) 10.dp else 0.dp, label = String()
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


@Composable
private fun DaySelection(
    selectedDays: Set<String> = emptySet(),
    onDaySelected: (Set<String>) -> Unit = {},
    checkedState: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DayOfWeekBar(
            selectedDays = selectedDays, isWorkoutReminder = true, onDaySelected = onDaySelected
        )
        InfoCheckboxRow(
            checkedState = checkedState, onCheckedChange = onCheckedChange, message = "Every Day"
        )

    }
}

@Composable
private fun TimePicker(
    hour: Int = 9,
    minute: String = "00",
    isAm: Boolean = true,
    onHourChange: (Int) -> Unit = {},
    onMinuteChange: (String) -> Unit = {},
    onAmChange: (Boolean) -> Unit = {}
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

@Composable
private fun InfoCheckboxRow(
    checkedState: Boolean, onCheckedChange: (Boolean) -> Unit, message: String
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
            text = message,
            fontSize = 14.sp,
            fontFamily = montserratFamily,
            color = GRAY04,
            fontWeight = FontWeight.SemiBold
        )
    }
}