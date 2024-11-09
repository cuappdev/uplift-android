package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.goalsetting.DeleteDialog
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSlider
import com.cornellappdev.uplift.ui.components.goalsetting.WorkoutReminders
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.montserratFamily

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
 * @param isOnboarding: whether the user is in onboarding
 * @param onNext: callback for when the next button is clicked
 * @param onSkip: callback for when the skip button is clicked
 * @return WorkoutReminderScreen composable
 */
@Composable
fun WorkoutReminderScreen(
    /* TODO: Replace functions with viewmodel calls */
    reminders: List<Reminder> = emptyList(),
    onRemindersChange: (List<Reminder>) -> Unit,
    goalValue: Float,
    onGoalValueChange: (Float) -> Unit,
    onBackClick: () -> Unit,
    isOnboarding: Boolean = false,
    onNext: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var addNewReminderState by remember { mutableStateOf(false) }
    var deleteDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            UpliftTopBarWithBack(
                title = "Goals",
                onBackClick = onBackClick,
                withBack = true
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(
                    top = padding.calculateTopPadding(),
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /* Groups the goal slider and workout reminders together */
            Column {
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
            if (isOnboarding) OnboardingButtons(onNext, onSkip)

        }
        DeleteDialog(
            deleteDialogOpen = deleteDialogOpen,
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

@Composable
private fun OnboardingButtons(onNext: () -> Unit, onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = 48.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UpliftButton(
            onClick = onNext,
            title = "Next",
            fontSize = 16f,
            width = 144.dp,
            height = 44.dp
        )
        Text(
            text = "Skip",
            fontFamily = montserratFamily,
            color = GRAY04,
            modifier = Modifier.clickable(onClick = onSkip)
        )
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
        onGoalValueChange = { sliderVal = it },
        onBackClick = {},
    )
}