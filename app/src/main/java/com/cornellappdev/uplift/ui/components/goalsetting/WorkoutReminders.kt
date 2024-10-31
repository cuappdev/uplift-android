package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.screens.Reminder
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.GRAY09
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * @param selectedReminder the reminder that is currently selected
 * @param onSelectedReminderChange callback for when the selected reminder changes
 * @param reminders the list of reminders
 * @param onRemindersChange callback for when the reminders change
 * @param addNewReminderState whether the user is currently adding a new reminder
 * @param onAddNewReminderStateChange callback for when the add new reminder state changes
 * @param openDelete callback for when the delete dialog should be opened
 * @return WorkoutReminders composable that allows the user to view and edit workout reminders
 * @sample WorkoutReminders(
 *    selectedReminder = Reminder("12:00 PM", setOf("Monday", "Wednesday", "Friday"), true),
 *    onSelectedReminderChange = {},
 *    reminders = listOf(
 *    Reminder("12:00 PM", setOf("M", "W", "F"), true),
 *    Reminder("9:00 AM", setOf("T", "Th"), false)
 *    ),
 *    onRemindersChange = {},
 *    addNewReminderState = false,
 *    onAddNewReminderStateChange = {},
 *    openDelete = {}
 *    )
 */
@Composable
fun WorkoutReminders(
    selectedReminder: Reminder?,
    onSelectedReminderChange: (Reminder?) -> Unit,
    reminders: List<Reminder>,
    onRemindersChange: (List<Reminder>) -> Unit,
    addNewReminderState: Boolean,
    onAddNewReminderStateChange: (Boolean) -> Unit,
    openDelete: () -> Unit
) {
    WorkoutReminderHeader()
    NewReminderButton(
        enabled = !addNewReminderState && selectedReminder == null,
        addNewReminderState = addNewReminderState,
        onAddNewReminderStateChange = {
            onSelectedReminderChange(null)
            onAddNewReminderStateChange(it)
        }
    )
    AnimatedVisibility(
        visible = addNewReminderState || selectedReminder != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        EditReminderSection(
            reminder = selectedReminder,
            addNewReminderState = addNewReminderState,
            onReminderSave = { updatedReminder ->
                if (selectedReminder != null) {
                    onRemindersChange(reminders.map {
                        if (it == selectedReminder) updatedReminder else it
                    })
                } else {
                    onRemindersChange(listOf(updatedReminder) + reminders)
                }
                onAddNewReminderStateChange(false)
                onSelectedReminderChange(null)
            },
            openDelete = openDelete

        )
    }
    ReminderCardList(
        reminders,
        onRemindersChange,
        onReminderClick = { reminder ->
            if (selectedReminder == null) {
                onSelectedReminderChange(reminder)
            }
        }
    )
}

/**
 * @return Header for the workout reminder section
 */
@Composable
private fun WorkoutReminderHeader() {
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

/**
 * @param enabled whether the button is enabled
 * @param addNewReminderState whether the user is currently adding a new reminder
 * @param onAddNewReminderStateChange callback for when the add new reminder state changes
 * @return  New reminder button that allows the user to add a new reminder
 * @sample NewReminderButton(
 *   enabled = true,
 *   addNewReminderState = false,
 *   onAddNewReminderStateChange = {}
 *   )
 */
@Composable
private fun NewReminderButton(
    enabled: Boolean,
    addNewReminderState: Boolean,
    onAddNewReminderStateChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (enabled) onAddNewReminderStateChange(!addNewReminderState)
            }
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