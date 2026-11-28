package com.cornellappdev.uplift.ui.screens.reminders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.RadioCheckButton
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reminders.ClassReminderInput
import com.cornellappdev.uplift.ui.components.reminders.DescriptionReminderSwitch
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.ui.viewmodels.reminders.ClassRemindersUiState
import com.cornellappdev.uplift.ui.viewmodels.reminders.ClassRemindersViewModel
import com.cornellappdev.uplift.ui.viewmodels.reminders.ReminderTimeOption
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * ClassReminderScreen is a composable that displays the class reminder settings screen.
 */
@Composable
fun ClassRemindersScreen(
    classRemindersViewModel: ClassRemindersViewModel = hiltViewModel()
) {
    val classRemindersUiState = classRemindersViewModel.collectUiStateValue()

    ClassReminderContent(
        uiState = classRemindersUiState,
        onBack = classRemindersViewModel::onBack,
        setToggle = classRemindersViewModel::setToggle,
        onOptionSelected = classRemindersViewModel::selectTimeOption,
        onHoursChanged = classRemindersViewModel::onHoursChanged,
        onMinutesChanged = classRemindersViewModel::onMinutesChanged,
        onDismissInvalidTimeDialog = classRemindersViewModel::onDismissInvalidTimeDialog
    )
}

@Composable
private fun ClassReminderContent(
    uiState: ClassRemindersUiState,
    onBack: () -> Unit,
    setToggle: (Boolean) -> Unit,
    onOptionSelected: (ReminderTimeOption) -> Unit,
    onHoursChanged: (String) -> Unit,
    onMinutesChanged: (String) -> Unit,
    onDismissInvalidTimeDialog: () -> Unit
) {
    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = stringResource(R.string.class_reminders_screen_title),
            onBackClick = onBack,
            withBack = true
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    top = padding.calculateTopPadding() + 24.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DescriptionReminderSwitch(
                    text = stringResource(R.string.class_reminders_header),
                    checked = uiState.toggledOn,
                    onCheckedChange = setToggle
                )
                Text(
                    text = stringResource(R.string.class_reminders_description),
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = GRAY03
                )
            }

            AnimatedVisibility(
                visible = uiState.toggledOn, enter = fadeIn(), exit = fadeOut()
            ) {
                ClassRemindersSettings(
                    timeOption = uiState.timeOption,
                    customHours = uiState.customHours,
                    customMinutes = uiState.customMinutes,
                    isMinutesError = uiState.isMinutesError,
                    onOptionSelected = onOptionSelected,
                    onHoursChanged = onHoursChanged,
                    onMinutesChanged = onMinutesChanged
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (uiState.showInvalidTimeDialog) {
            InvalidTimeDialog(onDismiss = onDismissInvalidTimeDialog)
        }
    }
}

@Composable
private fun ClassRemindersSettings(
    timeOption: ReminderTimeOption,
    customHours: String,
    customMinutes: String,
    isMinutesError: Boolean,
    onOptionSelected: (ReminderTimeOption) -> Unit,
    onHoursChanged: (String) -> Unit,
    onMinutesChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.time_before_class),
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = GRAY03
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RadioCheckButton(
                    stringResource(R.string.reminder_time_30_min),
                    selected = timeOption == ReminderTimeOption.THIRTY_MIN,
                    onButtonSelected = { onOptionSelected(ReminderTimeOption.THIRTY_MIN) }
                )

                RadioCheckButton(
                    stringResource(R.string.reminder_time_60_min),
                    selected = timeOption == ReminderTimeOption.SIXTY_MIN,
                    onButtonSelected = { onOptionSelected(ReminderTimeOption.SIXTY_MIN) }
                )
            }

            RadioCheckButton(
                stringResource(R.string.reminder_time_custom),
                selected = timeOption == ReminderTimeOption.CUSTOM,
                onButtonSelected = { onOptionSelected(ReminderTimeOption.CUSTOM) }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ClassReminderInput(
                        label = stringResource(R.string.reminder_input_hours),
                        placeholder = stringResource(R.string.reminder_input_placeholder),
                        enabled = timeOption == ReminderTimeOption.CUSTOM,
                        textValue = customHours,
                        onValueChange = onHoursChanged,
                        errorState = false
                    )

                    ClassReminderInput(
                        label = stringResource(R.string.reminder_input_minutes),
                        placeholder = stringResource(R.string.reminder_input_placeholder),
                        enabled = timeOption == ReminderTimeOption.CUSTOM,
                        textValue = customMinutes,
                        onValueChange = onMinutesChanged,
                        errorState = isMinutesError
                    )
                }

                if (isMinutesError && timeOption == ReminderTimeOption.CUSTOM) {
                    Text(
                        text = stringResource(R.string.reminder_invalid_minutes),
                        color = Color.Red,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Temporary Error Dialog when user wants to exit screen with invalid custom time
@Composable
private fun InvalidTimeDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit symbol",
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        },
        text = {
            Text(
                text = "Please input a valid time before saving.",
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = PRIMARY_BLACK,
                textAlign = TextAlign.Center
            )
        },
        containerColor = Color.White,
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                UpliftButton(
                    onClick = onDismiss,
                    text = "Continue",
                    containerColor = PRIMARY_BLACK,
                    contentColor = Color.White,
                    elevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun InvalidTimeDialogPreview() {
    UpliftTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            InvalidTimeDialog(onDismiss = {})
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ClassRemindersContentPreview() {
    var uiState by remember {
        mutableStateOf(
            ClassRemindersUiState(
                toggledOn = true,
                timeOption = ReminderTimeOption.CUSTOM,
                customHours = "1",
                customMinutes = "30",
                isMinutesError = false,
                showInvalidTimeDialog = false
            )
        )
    }

    ClassReminderContent(
        uiState = uiState,
        onBack = {
            val hours = uiState.customHours.toIntOrNull() ?: 0
            val minutes = uiState.customMinutes.toIntOrNull() ?: 0
            val hasInvalidCustomTime = uiState.toggledOn &&
                    uiState.timeOption == ReminderTimeOption.CUSTOM &&
                    (uiState.isMinutesError || (hours == 0 && minutes == 0))

            if (hasInvalidCustomTime) {
                uiState = uiState.copy(showInvalidTimeDialog = true)
            }
        },
        setToggle = { uiState = uiState.copy(toggledOn = it) },
        onOptionSelected = { uiState = uiState.copy(timeOption = it) },
        onHoursChanged = { input ->
            if (input.all { it.isDigit() } && input.length <= 2) {
                uiState = uiState.copy(customHours = input)
            }
        },
        onMinutesChanged = { input ->
            if (input.all { it.isDigit() } && input.length <= 2) {
                val minutesInt = input.toIntOrNull()
                val isError = minutesInt != null && minutesInt > 59
                uiState = uiState.copy(
                    customMinutes = input,
                    isMinutesError = isError
                )
            }
        },
        onDismissInvalidTimeDialog = {
            uiState = uiState.copy(showInvalidTimeDialog = false)
        }
    )

}
