package com.cornellappdev.uplift.ui.components.profile.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
private fun WorkoutsSectionContent(
    workoutsCompleted: Int,
    workoutGoal: Int,
    daysOfMonth: List<Int>,
    completedDays: List<Boolean>,
    reminderItems: List<ReminderItem>,
    historyItems: List<HistoryItem>,
    navigateToGoalsSection: () -> Unit,
    navigateToRemindersSection: () -> Unit,
    navigateToHistorySection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoalsSection(
            workoutsCompleted = workoutsCompleted,
            workoutGoal = workoutGoal,
            daysOfMonth = daysOfMonth,
            completedDays = completedDays,
            onClick = navigateToGoalsSection,
        )
        Spacer(modifier = Modifier.height(24.dp))

        HistorySection(
            historyItems = historyItems,
            onClick = navigateToHistorySection,
            modifier = Modifier.weight(1f)
        )
    }
}