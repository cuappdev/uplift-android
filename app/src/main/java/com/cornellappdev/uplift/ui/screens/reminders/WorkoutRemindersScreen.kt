package com.cornellappdev.uplift.ui.screens.reminders

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.NotificationPermissionHandler
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reminders.WorkoutReminders
import com.cornellappdev.uplift.ui.components.reminders.goalsetting.DeleteDialog
import com.cornellappdev.uplift.ui.viewmodels.reminders.WorkoutRemindersUiState
import com.cornellappdev.uplift.ui.viewmodels.reminders.WorkoutRemindersViewModel
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
 * WorkoutReminderSettingsScreen is a composable that displays the workout reminder settings screen.
 */
@Composable
fun WorkoutReminderSettingsScreen(
    workoutRemindersViewModel: WorkoutRemindersViewModel = hiltViewModel()
) {
    val workoutRemindersUiState = workoutRemindersViewModel.collectUiStateValue()
    WorkoutReminderScreen(
        uiState = workoutRemindersUiState,
        isOnboarding = false,
        onRemindersChange = workoutRemindersViewModel::onRemindersChange,
        onSelectedReminderChange = workoutRemindersViewModel::onSelectedReminderChange,
        onAddNewReminderStateChange = workoutRemindersViewModel::onAddNewReminderStateChange,
        onDeleteDialogOpenChange = workoutRemindersViewModel::onDeleteDialogOpenChange,
        onDeleteConfirm = workoutRemindersViewModel::onDeleteConfirmed,
        onBackClick = workoutRemindersViewModel::onBack
    )
    NotificationPermissionHandler()
}

@Composable
fun WorkoutReminderScreen(
    uiState: WorkoutRemindersUiState = WorkoutRemindersUiState(),
    reminders: List<Reminder> = uiState.reminders,
    onRemindersChange: (List<Reminder>) -> Unit = {},
    onSelectedReminderChange: (Reminder?) -> Unit = {},
    onAddNewReminderStateChange: (Boolean) -> Unit = {},
    onDeleteDialogOpenChange: (Boolean) -> Unit = {},
    onDeleteConfirm: () -> Unit = {},
    onBackClick: () -> Unit = {},
    isOnboarding: Boolean = false,
    onNext: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val activeReminders = reminders.ifEmpty { uiState.reminders }

    WorkoutReminderContent(
        reminders = activeReminders,
        selectedReminder = uiState.selectedReminder,
        addNewReminderState = uiState.addNewReminderState,
        deleteDialogOpen = uiState.deleteDialogOpen,
        isOnboarding = isOnboarding,
        onSelectedReminderChange = onSelectedReminderChange,
        onAddNewReminderStateChange = onAddNewReminderStateChange,
        onDeleteDialogOpenChange = onDeleteDialogOpenChange,
        onDeleteConfirm = onDeleteConfirm,
        onRemindersChange = onRemindersChange,
        onBackClick = onBackClick,
        onNext = onNext,
        onSkip = onSkip
    )
}

@Composable
private fun WorkoutReminderContent(
    reminders: List<Reminder>,
    selectedReminder: Reminder?,
    addNewReminderState: Boolean,
    deleteDialogOpen: Boolean,
    isOnboarding: Boolean,
    onSelectedReminderChange: (Reminder?) -> Unit,
    onAddNewReminderStateChange: (Boolean) -> Unit,
    onDeleteDialogOpenChange: (Boolean) -> Unit,
    onDeleteConfirm: () -> Unit,
    onRemindersChange: (List<Reminder>) -> Unit,
    onBackClick: () -> Unit,
    onNext: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            UpliftTopBarWithBack(
                title = stringResource(id = R.string.workout_reminders_title),
                onBackClick = onBackClick,
                withBack = true
            )
        },
        bottomBar = {
            if (isOnboarding) {
                OnboardingButtons(onNext, onSkip)
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp),
        ) {
            if (!isOnboarding) {
                WorkoutReminders(
                    selectedReminder = selectedReminder,
                    onSelectedReminderChange = onSelectedReminderChange,
                    reminders = reminders,
                    onRemindersChange = onRemindersChange,
                    addNewReminderState = addNewReminderState,
                    onAddNewReminderStateChange = onAddNewReminderStateChange,
                    openDelete = { onDeleteDialogOpenChange(true) }
                )
            }
        }

        if (deleteDialogOpen) {
            DeleteDialog(
                deleteDialogOpen = deleteDialogOpen,
                onConfirm = onDeleteConfirm,
                onDismiss = { onDeleteDialogOpenChange(false) }
            )
        }
    }
}

@Composable
fun OnboardingButtons(onNext: () -> Unit, onSkip: () -> Unit) {
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
            text = "Next",
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
private fun WorkoutReminderScreenPreview() {
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var addNewReminderState by remember { mutableStateOf(false) }
    var deleteDialogOpen by remember { mutableStateOf(false) }
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

    val uiState = WorkoutRemindersUiState(
        reminders = reminders,
        selectedReminder = selectedReminder,
        addNewReminderState = addNewReminderState,
        deleteDialogOpen = deleteDialogOpen
    )

    WorkoutReminderScreen(
        uiState = uiState,
        onRemindersChange = { updated ->
            reminders.clear()
            reminders.addAll(updated)
        },
        onSelectedReminderChange = { selectedReminder = it },
        onAddNewReminderStateChange = { addNewReminderState = it },
        onDeleteDialogOpenChange = { deleteDialogOpen = it },
        onDeleteConfirm = {
            selectedReminder?.let { reminder ->
                reminders.remove(reminder)
            }
            deleteDialogOpen = false
            selectedReminder = null
            addNewReminderState = false
        },
        onBackClick = {},
        isOnboarding = false
    )
}