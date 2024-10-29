package com.cornellappdev.uplift.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.goalsetting.DeleteDialog
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSlider
import com.cornellappdev.uplift.ui.components.goalsetting.WorkoutReminders

/**
 * @param time: time of the reminder
 * @param days: days of the week the reminder is set for
 * @param enabled: whether the reminder is enabled
 * @return Reminder data class
 */
data class Reminder(
    val time: String,
    val days: Set<String>,
    val enabled: Boolean
)

/**
 * @param reminders: list of reminders
 * @param onRemindersChange: callback for when reminders are changed
 * @param goalValue: value of the goal slider
 * @param onGoalValueChange: callback for when the goal slider value is changed
 * @param onBackClick: callback for when the back button is clicked
 * @return WorkoutReminderScreen composable
 */
@Composable
fun WorkoutReminderScreen(
    reminders: List<Reminder> = emptyList(),
    onRemindersChange: (List<Reminder>) -> Unit = {},
    goalValue: Float = 1f,
    onGoalValueChange: (Float) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var addNewReminderState by remember { mutableStateOf(false) }
    var deleteDialogOpen by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Goals",
            onBackClick = onBackClick,
            withBack = true
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(
                    top = padding.calculateTopPadding(),
                )
                .verticalScroll(rememberScrollState()),
        ) {
            GoalSlider(value = goalValue, onValueChange = onGoalValueChange)

            WorkoutReminders(
                selectedReminder = selectedReminder,
                onSelectedReminderChange = { selectedReminder = it },
                reminders = reminders,
                onRemindersChange = onRemindersChange,
                addNewReminderState = addNewReminderState,
                onAddNewReminderStateChange = { addNewReminderState = it },
                openDelete = { deleteDialogOpen = true }
            )
        }
        AnimatedVisibility(
            visible = deleteDialogOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DeleteDialog(
                onConfirm = {
                    selectedReminder?.let { reminder ->
                        onRemindersChange(reminders.filter { it != reminder })
                    }
                    deleteDialogOpen = false
                    selectedReminder = null
                    addNewReminderState = false
                },
                onDismiss = { deleteDialogOpen = false }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WorkoutReminderScreenPreview() {
    var sliderVal by remember { mutableFloatStateOf(1f) }
    val reminders = remember {
        mutableStateListOf(
            Reminder(time = "9:00 AM", days = setOf("M", "T", "W", "Th", "F"), enabled = true),
            Reminder(time = "3:00 PM", days = setOf("Sa", "Su"), enabled = true),
            Reminder(time = "12:00 PM", days = setOf("M", "W", "F"), enabled = false),
            Reminder(
                time = "6:00 AM",
                days = setOf("T", "W", "Th", "F", "Sa", "Su"),
                enabled = true
            ),
            Reminder(
                time = "8:00 PM",
                days = setOf("M", "T", "W", "Th", "F", "Sa", "Su"),
                enabled = true
            )
        )
    }
    WorkoutReminderScreen(
        reminders = reminders,
        onRemindersChange = { updatedReminders ->
            reminders.clear()
            reminders.addAll(updatedReminders)
        },
        goalValue = sliderVal,
        onGoalValueChange = { sliderVal = it }
    )
}