package com.cornellappdev.uplift.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSlider
import com.cornellappdev.uplift.ui.screens.reminders.OnboardingButtons
import com.cornellappdev.uplift.ui.viewmodels.onboarding.ProfileCreationViewModel

@Composable
fun GoalsOnboardingScreen(
    viewModel: ProfileCreationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    GoalsOnboardingScreenContent(
        goalValue = uiState.goal,
        onGoalValueChange = viewModel::updateGoals,
        onNext = viewModel::onNext,
        onSkip = viewModel::onSkip
    )
}

@Composable
private fun GoalsOnboardingScreenContent(
    goalValue: Float,
    onGoalValueChange: (Float) -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Scaffold(
        topBar = {
            UpliftTopBar(
                title = "Set your Goals.",
            )
        },
        bottomBar = {
            OnboardingButtons(onNext, onSkip)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding(),
    ) { padding ->
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GoalSlider(value = goalValue, onValueChange = onGoalValueChange, isOnboarding = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalsScreenPreview() {
    var sliderVal by remember { mutableFloatStateOf(3f) }
    GoalsOnboardingScreenContent(
        goalValue = sliderVal,
        onGoalValueChange = { sliderVal = it },
        onNext = {},
        onSkip = {}
    )
}