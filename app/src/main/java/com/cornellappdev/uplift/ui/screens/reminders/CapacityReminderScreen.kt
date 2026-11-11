package com.cornellappdev.uplift.ui.screens.reminders

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityReminderLoading
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityReminderSwitch
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityThreshold
import com.cornellappdev.uplift.ui.components.capacityreminder.LocationsToRemind
import com.cornellappdev.uplift.ui.components.capacityreminder.ReminderDays
import com.cornellappdev.uplift.ui.components.general.NotificationPermissionHandler
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.viewmodels.reminders.CapacityRemindersViewModel
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Screen for the capacity reminder feature.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CapacityReminderScreen(
    capacityRemindersViewModel: CapacityRemindersViewModel = hiltViewModel(),
) {
    val capacityRemindersUiState = capacityRemindersViewModel.collectUiStateValue()
    val checked = capacityRemindersUiState.toggledOn
    val initialSelectedDays = capacityRemindersUiState.selectedDays
    val capacityThreshold = capacityRemindersUiState.capacityThreshold
    val initialSelectedGyms = capacityRemindersUiState.selectedGyms
    if (capacityRemindersUiState.showUnsavedChangesDialog) {
        UnsavedChangesDialog(
            onSave = capacityRemindersViewModel::saveChanges,
            onDismiss = capacityRemindersViewModel::onDismissDialog,
            onDiscard = capacityRemindersViewModel::onConfirmDiscard
        )
    }
    CapacityRemindersContent(
        checked,
        initialSelectedDays,
        capacityThreshold,
        initialSelectedGyms,
        capacityRemindersUiState.isLoading,
        capacityRemindersViewModel::setToggle,
        capacityRemindersViewModel::setSelectedDays,
        capacityRemindersViewModel::setCapacityThreshold,
        capacityRemindersViewModel::setSelectedGyms,
        capacityRemindersViewModel::onBack,
        capacityRemindersViewModel::saveChanges,
        capacityRemindersUiState.saveSuccess,
        capacityRemindersUiState.error
    )
    NotificationPermissionHandler()
}

@Composable
private fun UnsavedChangesDialog(
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onDiscard: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit symbol",
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(16.dp))
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-15).dp, x = 15.dp)
                ) {
                    Icon(
                    painter = painterResource(id=R.drawable.ic_close),
                        contentDescription = "Exit symbol",
                        modifier = Modifier
                            .height(32.dp)
                            .width(32.dp)
                    )
                }
            }
        },
        text = {
            Text(
                text = "Your unsaved changes will be lost. Save before closing?",
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
                    onClick = onSave,
                    text = "Save",
                    containerColor = PRIMARY_BLACK,
                    contentColor = Color.White,
                    elevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                UpliftButton(onClick = onDiscard,
                    text = "Continue",
                    containerColor = Color.White,
                    elevation = 0.dp,
                    modifier = Modifier.fillMaxWidth())
            }
        },
    )
}

@Composable
private fun CapacityRemindersContent(
    switchChecked: Boolean,
    initialSelectedDays: Set<String>,
    capacityThreshold: Float,
    initialSelectedGyms: Set<String>,
    isLoading: Boolean,
    setToggle: (Boolean) -> Unit,
    setSelectedDays: (Set<String>) -> Unit,
    setCapacityThreshold: (Float) -> Unit,
    setSelectedGyms: (Set<String>) -> Unit,
    onBack: () -> Unit,
    saveChanges: () -> Unit,
    saveSuccess: Boolean,
    error: String?,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                UpliftTopBarWithBack(
                    title = "Capacity Reminder",
                    onBackClick = onBack
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(
                        top = padding.calculateTopPadding() + 24.dp, start = 16.dp, end = 16.dp
                    ), verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CapacityReminderSwitch(
                        checked = switchChecked,
                        onCheckedChange = setToggle
                    )
                    Text(
                        text = "Uplift will send you a notification when gyms dip below the set capacity",
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = GRAY03
                    )
                }
                AnimatedVisibility(
                    visible = switchChecked, enter = fadeIn(), exit = fadeOut()
                ) {
                    CapacityRemindersSettings(
                        initialSelectedDays,
                        setSelectedDays,
                        capacityThreshold,
                        setCapacityThreshold,
                        initialSelectedGyms,
                        setSelectedGyms,
                        saveChanges,
                        isLoading,
                        saveSuccess
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                UpliftButton(
                    text = when {
                        isLoading -> "Saving..."
                        saveSuccess -> "Saved"
                        else -> "Save Changes"
                    },
                    onClick = saveChanges,
                    enabled = !isLoading && !saveSuccess,
                    containerColor = PRIMARY_BLACK,
                    contentColor = Color.White,
                    disabledContainerColor = GRAY03,
                    disabledContentColor = Color.White,
                    elevation = 0.dp,
                    leadingIcon = {
                        if (saveSuccess) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = "Saved",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    },
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CapacityReminderLoading()
        }
    }
}

@Composable
private fun CapacityRemindersSettings(
    initialSelectedDays: Set<String>,
    setSelectedDays: (Set<String>) -> Unit,
    capacityThreshold: Float,
    setCapacityThreshold: (Float) -> Unit,
    initialSelectedGyms: Set<String>,
    setSelectedGyms: (Set<String>) -> Unit,
    saveChanges: () -> Unit,
    isLoading: Boolean,
    saveSuccess: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReminderDays(initialSelectedDays, setSelectedDays)
        CapacityThreshold(
            capacityThreshold,
            setCapacityThreshold
        )
        LocationsToRemind(
            initialSelectedGyms,
            setSelectedGyms
        )
        Spacer(modifier = Modifier.height(32.dp))
        UpliftButton(
            text = when {
                isLoading -> "Saving..."
                saveSuccess -> "Saved"
                else -> "Save Changes"
            },
            onClick = saveChanges,
            enabled = !isLoading && !saveSuccess,
            containerColor = PRIMARY_BLACK,
            contentColor = Color.White,
            disabledContainerColor = GRAY02,
            disabledContentColor = Color.White,
            elevation = 0.dp,
            leadingIcon = {
                if (saveSuccess) {
                    Icon(
                        painter=painterResource(id=R.drawable.ic_check),
                        contentDescription = "Saved",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            modifier = Modifier.wrapContentWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CapacityReminderScreenPreview() {
    var checked by remember { mutableStateOf(true) }
    var sliderVal by remember { mutableFloatStateOf(0.5f) }
    var selectedDays by remember { mutableStateOf(setOf("M", "Tu", "W", "Th", "F")) }
    var selectedGyms by remember {
        mutableStateOf(
            setOf(
                "Teagle Up",
                "Teagle Down",
                "Helen Newman"
            )
        )
    }
    CapacityRemindersContent(
        switchChecked = checked,
        initialSelectedDays = selectedDays,
        capacityThreshold = sliderVal,
        initialSelectedGyms = selectedGyms,
        isLoading = false,
        setToggle = { checked = it },
        setSelectedDays = { selectedDays = it },
        setCapacityThreshold = { sliderVal = it },
        setSelectedGyms = { selectedGyms = it },
        onBack = {},
        saveChanges = {},
        saveSuccess = false,
        error = null
    )
}
