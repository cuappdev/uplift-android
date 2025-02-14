package com.cornellappdev.uplift.ui.components.goalsetting

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch
import com.cornellappdev.uplift.ui.screens.reminders.Reminder
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * @param reminders: list of reminders
 * @param onRemindersChange: callback for when reminders are changed
 * @param onReminderClick: callback for when a reminder is clicked
 * @return Column list of reminder cards
 * @see ReminderCard
 */
@Composable
fun ReminderCardList(
    reminders: List<Reminder>,
    onRemindersChange: (List<Reminder>) -> Unit,
    onReminderClick: (Reminder) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        reminders.forEachIndexed { index, reminder ->
            ReminderCard(
                dayOfWeek = mapSelectedDays(reminder.days),
                time = reminder.time,
                enabled = reminder.enabled,
                onEnabledChange = { isEnabled ->
                    // Create a new list with the updated enabled state
                    val updatedReminders = reminders.toMutableList().apply {
                        this[index] = this[index].copy(enabled = isEnabled)
                    }
                    // Update the list of reminders
                    onRemindersChange(updatedReminders)
                },
                onReminderClick = { onReminderClick(reminder) }
            )
        }
    }
}

/**
 * @param dayOfWeek: day of the week
 * @param time: time of the reminder
 * @param enabled: whether the reminder is enabled
 * @param onEnabledChange: callback for when the reminder is enabled
 * @param onReminderClick: callback for when the reminder is clicked
 * @return ReminderCard composable with day of the week, time, and enabled state
 * @sample ReminderCard(
 *    dayOfWeek = "M",
 *    time = "8:00 AM",
 *    enabled = true,
 *    onEnabledChange = {},
 *    onReminderClick = {}
 *    )
 */
@Composable
fun ReminderCard(
    dayOfWeek: String,
    time: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onReminderClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onReminderClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReminderCardTopRow(dayOfWeek, enabled, onEnabledChange)

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
private fun ReminderCardTopRow(
    dayOfWeek: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
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
}

/**
 * @param selectedDays: set of selected days
 * @return String representation of selected days
 */
fun mapSelectedDays(selectedDays: Set<String>): String {
    val length = selectedDays.size
    val dayOrder = listOf("M", "T", "W", "Th", "F", "Sa", "Su")
    val sortedDays = selectedDays.sortedBy { dayOrder.indexOf(it) }
    return when {
        length == 1 -> mapAbbreviatedDay(selectedDays.first())
        length == 7 -> "Every Day"
        length == 0 -> "Never"
        selectedDays == setOf("M", "T", "W", "Th", "F") -> "Weekdays"
        selectedDays == setOf("Sa", "Su") -> "Weekends"
        else -> {
            sortedDays.joinToString("  ")
        }
    }
}

/**
 * @param day: abbreviated day
 * @return full day of the week
 */
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