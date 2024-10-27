package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch
import com.cornellappdev.uplift.ui.screens.Reminder
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.GRAY09
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily


@Composable
fun WorkoutReminders(
    reminders: List<Reminder> = emptyList(),
    onRemindersChange: (List<Reminder>) -> Unit = {},
    enabled: Boolean = true,
    onEnabledChange: (Boolean) -> Unit = {},
    addNewReminderState: Boolean = false,
    onAddNewReminderStateChange: (Boolean) -> Unit = {},
) {
    // Reminders directly inside the main column
    WorkoutReminderHeader()
    NewReminderButton(
        enabled = enabled,
        onEnabledChange = onEnabledChange,
        addNewReminderState = addNewReminderState,
        onAddNewReminderStateChange = onAddNewReminderStateChange
    )
    AnimatedVisibility(visible = addNewReminderState, enter = fadeIn(), exit = fadeOut()) {
        EditReminderSection(
            addNewReminderState = addNewReminderState,
            onAddNewReminderStateChange = onAddNewReminderStateChange,
            reminders = reminders,
            onRemindersChange = onRemindersChange
        )

    }
    ReminderCardList(reminders)

}

@Composable
fun DeleteButton(
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .width(120.dp)
            .height(40.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_trash),
                contentDescription = "Delete",
            )
            Text(
                text = "Delete",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@Composable
fun SubmitReminderButton(
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .width(120.dp)
            .height(40.dp),
        color = PRIMARY_BLACK,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Done",
                tint = Color.White
            )
            Text(
                text = "Done",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

fun mapSelectedDays(selectedDays: Set<String>): String {
    val length = selectedDays.size
    // Define the order of days from Monday to Sunday
    val dayOrder = listOf("M", "T", "W", "Th", "F", "Sa", "Su")

    // Sort the selectedDays based on the predefined order
    val sortedDays = selectedDays.sortedBy { dayOrder.indexOf(it) }
    return when {
        length == 1 -> mapAbbreviatedDay(selectedDays.first())
        length == 7 -> "Every Day"
        selectedDays == setOf("M", "T", "W", "Th", "F") -> "Weekdays"
        selectedDays == setOf("Sa", "Su") -> "Weekends"
        else -> {
            sortedDays.joinToString("  ")
        }
    }
}

fun mapAbbreviatedDay(day: String): String {
    return when (day) {
        "M" -> "Monday"
        "T" -> "Tuesday"
        "W" -> "Wednesday"
        "Th" -> "Thursday"
        "F" -> "Friday"
        "Sa" -> "Saturday"
        "Su" -> "Sunday"
        else -> ""
    }
}

@Composable
fun ReminderCardList(
    reminders: List<Reminder> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // Render Reminder list
        reminders.forEachIndexed { index, reminder ->
            ReminderCard(dayOfWeek = mapSelectedDays(reminder.days),
                time = reminder.time,
                enabled = reminder.enabled,
                onEnabledChange = { isEnabled ->
                    // Update the enabled state for the specific reminder
                    reminders[index].onEnabledChange(isEnabled)
                })
        }
    }
}

@Composable
fun ReminderCard(
    dayOfWeek: String, time: String, enabled: Boolean, onEnabledChange: (Boolean) -> Unit = {}
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(50.dp)
                        .border(1.dp, GRAY02, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        fontFamily = montserratFamily,
                        fontSize = 24.sp,
                        color = PRIMARY_BLACK,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
                    )
                }

                ReminderSwitch(checked = enabled, onCheckedChange = onEnabledChange)
            }

            Text(
                text = time,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp)
            )
        }
    }
}

@Composable
fun WorkoutReminderHeader() {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "WORKOUT REMINDERS",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            color = PRIMARY_BLACK,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Get reminders on workout days to stay on track!",
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            color = GRAY04,
            fontWeight = FontWeight.Medium

        )
    }
}

@Composable
fun NewReminderButton(
    enabled: Boolean = true,
    onEnabledChange: (Boolean) -> Unit = {},
    addNewReminderState: Boolean = false,
    onAddNewReminderStateChange: (Boolean) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = {
                onEnabledChange(!enabled)
                onAddNewReminderStateChange(!addNewReminderState)
            })
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.ic_circle_add
            ), contentDescription = "Add Reminder", tint = if (enabled) PRIMARY_BLACK else GRAY09
        )
        Text(
            text = "New Reminder",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            color = if (enabled) PRIMARY_BLACK else GRAY09,
            fontWeight = FontWeight.Medium
        )
    }

}