package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GoalsSection(
    workoutsCompleted: Int,
    workoutGoal: Int,
    daysOfMonth: List<Int>,
    completedDays: List<Boolean>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SectionTitleText("My Goals", onClick)
        Spacer(modifier = Modifier.height(12.dp))
        WorkoutProgressArc(
            workoutsCompleted,
            workoutGoal
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeeklyProgressTracker(
            daysOfMonth,
            completedDays
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalsSectionPreview() {
    val daysOfMonth = (25..31).toList()
    val completedDays = listOf(false, false, true, false, true, false, false)
    GoalsSection(
        workoutsCompleted = 3,
        workoutGoal = 5,
        daysOfMonth = daysOfMonth,
        completedDays = completedDays,
        onClick = {}
    )
}