package com.cornellappdev.uplift.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cornellappdev.uplift.ui.screens.reminders.WorkoutReminderScreen
import com.cornellappdev.uplift.ui.viewmodels.onboarding.ProfileCreationViewModel

@Composable
fun WorkoutReminderOnboardingScreen(
    viewModel: ProfileCreationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val goalValue = uiState.goal

    WorkoutReminderScreen(
        goalValue = goalValue,
        isOnboarding = true,
        onGoalValueChange = { viewModel.updateGoals(it) },
        onBackClick = { viewModel.onBackClick() },
        onNext = { viewModel.onNext() },
        onSkip = { viewModel.onSkip() },
    )
}