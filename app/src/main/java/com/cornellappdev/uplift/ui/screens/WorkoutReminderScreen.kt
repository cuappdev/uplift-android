package com.cornellappdev.uplift.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.goalsetting.EditReminderCard
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSlider
import com.cornellappdev.uplift.ui.components.goalsetting.NewReminderButton
import com.cornellappdev.uplift.ui.components.goalsetting.ReminderCard
import com.cornellappdev.uplift.ui.components.goalsetting.WorkoutReminderHeader
import com.cornellappdev.uplift.ui.components.goalsetting.WorkoutReminders
import com.cornellappdev.uplift.ui.components.goalsetting.mapSelectedDays

data class Reminder(
    val time: String,
    val days: Set<String>,
    val enabled: Boolean,
    val onEnabledChange: (Boolean) -> Unit
)

@Composable
fun WorkoutReminderScreen(
    reminders: List<Reminder> = emptyList(),
    onRemindersChange: (List<Reminder>) -> Unit = {},
    goalValue: Float = 1f,
    onGoalValueChange: (Float) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var enabled by remember { mutableStateOf(true) }
    var addNewReminderState by remember { mutableStateOf(true) }

    // Scaffold for the top bar
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
            // Goal Slider
            GoalSlider(value = goalValue, onValueChange = onGoalValueChange)

            WorkoutReminders(
                reminders = reminders,
                onRemindersChange = onRemindersChange,
                enabled = enabled,
                onEnabledChange = { enabled = it },
                addNewReminderState = addNewReminderState,
                onAddNewReminderStateChange = { addNewReminderState = it }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WorkoutReminderScreenPreview() {
    var sliderVal by remember { mutableFloatStateOf(1f) }
    var enabled1 by remember { mutableStateOf(true) }
    var enabled2 by remember { mutableStateOf(true) }
    var enabled3 by remember { mutableStateOf(false) }
    var enabled4 by remember { mutableStateOf(true) }
    var enabled5 by remember { mutableStateOf(true) }
    var reminders by remember {
        mutableStateOf(
            listOf(
                Reminder(
                    time = "9:00 AM",
                    days = setOf("M", "T", "W", "Th", "F"),
                    enabled = enabled1,
                    onEnabledChange = {
                        enabled1 = it
                    }
                ),
                Reminder(
                    time = "3:00 PM",
                    days = setOf("Sa", "Su"),
                    enabled = enabled2,
                    onEnabledChange = {
                        enabled2 = it
                    }
                ),
                Reminder(
                    time = "12:00 PM",
                    days = setOf("M", "W", "F"),
                    enabled = enabled3,
                    onEnabledChange = {
                        enabled3 = it
                    }
                ),
                Reminder(
                    time = "6:00 AM",
                    days = setOf("T", "W", "Th", "F", "Sa", "Su"),
                    enabled = enabled4,
                    onEnabledChange = {
                        enabled4 = it
                    }
                ),
                Reminder(
                    time = "8:00 PM",
                    days = setOf("M", "T", "W", "Th", "F", "Sa", "Su"),
                    enabled = enabled5,
                    onEnabledChange = {
                        enabled5 = it
                    }
                )
            )
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        WorkoutReminderScreen(
            reminders = reminders,
            onRemindersChange = { reminders = it },
            goalValue = sliderVal,
            onGoalValueChange = { sliderVal = it }
        )
    }
}