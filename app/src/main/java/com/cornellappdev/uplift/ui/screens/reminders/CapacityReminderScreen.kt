package com.cornellappdev.uplift.ui.screens.reminders

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityReminderSwitch
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityThreshold
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.ui.components.capacityreminder.LocationsToRemind
import com.cornellappdev.uplift.ui.components.capacityreminder.ReminderDays
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.viewmodels.reminders.CapacityRemindersViewModel

/**
 * Screen for the capacity reminder feature.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CapacityReminderScreen(
    capacityRemindersViewModel: CapacityRemindersViewModel = hiltViewModel()
) {
    val capacityRemindersUiState = capacityRemindersViewModel.collectUiStateValue()
    val checked = capacityRemindersUiState.toggledOn
    val initialSelectedDays = capacityRemindersUiState.selectedDays
    val capacityThreshold = capacityRemindersUiState.capacityThreshold
    val initialSelectedGyms = capacityRemindersUiState.selectedGyms
    CapacityRemindersContent(
        checked,
        initialSelectedDays,
        capacityThreshold,
        initialSelectedGyms,
        capacityRemindersViewModel::setToggle,
        capacityRemindersViewModel::setSelectedDays,
        capacityRemindersViewModel::setCapacityThreshold,
        capacityRemindersViewModel::setSelectedGyms,
        capacityRemindersViewModel::onBack,
        capacityRemindersViewModel::saveChanges
    )
}

@Composable
private fun CapacityRemindersContent(
    switchChecked: Boolean,
    initialSelectedDays: Set<String>,
    capacityThreshold: Float,
    initialSelectedGyms: Set<String>,
    setToggle: (Boolean) -> Unit,
    setSelectedDays: (Set<String>) -> Unit,
    setCapacityThreshold: (Float) -> Unit,
    setSelectedGyms: (Set<String>) -> Unit,
    onBack: () -> Unit,
    saveChanges: () -> Unit
) {
    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Capacity Reminder",
            onBackClick = onBack
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding() + 24.dp, start = 16.dp, end = 16.dp
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    saveChanges
                )
            }
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
            text = "Save",
            onClick = {
                saveChanges()
            },
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
        setToggle = { checked = it },
        setSelectedDays = { selectedDays = it },
        setCapacityThreshold = { sliderVal = it },
        setSelectedGyms = { selectedGyms = it },
        onBack = {},
        saveChanges = {}
    )
}
