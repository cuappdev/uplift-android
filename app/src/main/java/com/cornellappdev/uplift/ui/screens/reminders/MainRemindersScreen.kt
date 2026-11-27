package com.cornellappdev.uplift.ui.screens.reminders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.PageOption
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.viewmodels.reminders.MainRemindersViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Screen displaying all reminder page options
 */
@Composable
fun MainRemindersScreen(
    mainRemindersViewModel: MainRemindersViewModel = hiltViewModel(),
) {
    MainReminderContent(
        onBack = mainRemindersViewModel::onBack,
        onWorkoutRemindersPressed = mainRemindersViewModel::onWorkoutRemindersPressed,
        onCapacityRemindersPressed = mainRemindersViewModel::onCapacityRemindersPressed,
    )
}

@Composable
fun MainReminderContent(
    onBack: () -> Unit,
    onWorkoutRemindersPressed: () -> Unit,
    onCapacityRemindersPressed: () -> Unit
) {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = stringResource(id = R.string.reminders_title),
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
            PageOption(
                icon = R.drawable.gym_simple,
                title = stringResource(id = R.string.workout_reminders_title),
                onClick = onWorkoutRemindersPressed,
            )
            HorizontalDivider(color = GRAY01)
            PageOption(
                icon = R.drawable.capacities,
                title = stringResource(id = R.string.capacity_reminders_switch_text),
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
        onWorkoutRemindersPressed = {},
        onCapacityRemindersPressed = {},
    )
}