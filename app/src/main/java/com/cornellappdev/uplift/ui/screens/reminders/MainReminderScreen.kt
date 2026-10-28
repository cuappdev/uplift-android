package com.cornellappdev.uplift.ui.screens.reminders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.PageOption
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.viewmodels.reminders.MainReminderViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainReminderScreen(
    mainReminderViewModel: MainReminderViewModel = hiltViewModel(),
) {
    MainReminderContent(
        onBack = mainReminderViewModel::onBack,
        onClassReminderPressed = mainReminderViewModel::onClassReminderPressed,
        onWorkoutRemindersPressed = mainReminderViewModel::onWorkoutRemindersPressed,
        onCapacityRemindersPressed = mainReminderViewModel::onCapacityRemindersPressed,
    )
}

@Composable
fun MainReminderContent(
    onBack: () -> Unit,
    onClassReminderPressed: () -> Unit,
    onWorkoutRemindersPressed: () -> Unit,
    onCapacityRemindersPressed: () -> Unit
) {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Reminders",
            onBackClick = onBack,
            withBack = true
        )
    }) {  padding ->
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
                PageOption(
                    icon = R.drawable.ic_classes_inactive,
                    title = "Class Reminders",
                    onClick = onClassReminderPressed,
                )
                Divider(color = GRAY01)
                PageOption(
                    icon = R.drawable.gym_simple,
                    title = "Workout Reminders",
                    onClick = onWorkoutRemindersPressed,
                )
                Divider(color = GRAY01)
                PageOption(
                    icon = R.drawable.capacities,
                    title = "Capacity Reminders",
                    onClick = onCapacityRemindersPressed,
                )
        }
    }
}

@Preview
@Composable
private fun MainReminderScreenPreview() {
    MainReminderContent(
        onBack = {},
        onClassReminderPressed = {},
        onWorkoutRemindersPressed = {},
        onCapacityRemindersPressed = {},
    )
}