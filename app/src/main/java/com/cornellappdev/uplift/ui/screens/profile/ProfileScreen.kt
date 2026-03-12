package com.cornellappdev.uplift.ui.screens.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.profile.workouts.GoalsSection
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItem
import com.cornellappdev.uplift.ui.components.profile.workouts.HistorySection
import com.cornellappdev.uplift.ui.components.profile.ProfileHeaderSection
import com.cornellappdev.uplift.ui.components.profile.workouts.ReminderItem
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileUiState
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    ProfileScreenContent(uiState,viewModel::toSettings,viewModel::toGoals, viewModel::toHistory)

}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileUiState,
    toSettings: () -> Unit,
    toGoals: () -> Unit,
    toHistory: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            ProfileScreenTopBar(navigateToSettings = toSettings)
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
        ) {
            ProfileHeaderSection(
                name = uiState.name,
                gymDays = uiState.totalGymDays,
                streaks = uiState.activeStreak,
                profilePictureUri = uiState.profileImage,
                onPhotoSelected = {},
                netId = uiState.netId
            )
            WorkoutsSectionContent(
                workoutsCompleted = uiState.workoutsCompleted,
                workoutGoal = uiState.workoutGoal,
                daysOfMonth = uiState.daysOfMonth,
                completedDays = uiState.completedDays,
                reminderItems= emptyList(), //implement
                historyItems = uiState.historyItems,
                navigateToGoalsSection = toGoals,
                navigateToRemindersSection = { /* TODO: Replace {} with viewmodel nav call */ },
                navigateToHistorySection = toHistory
            )

        }
    }
}

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

//TODO: Implement AchievementsSection
@Composable
private fun AchievementsSectionContent() {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileScreenTopBar(
    navigateToSettings: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                fontFamily = montserratFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                ambientColor = GRAY01
            ),
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Profile Settings"
                )
            }
        }
    )
}

@Preview
@Composable
private fun ProfileScreenContentPreview() {
    ProfileScreenContent(
        uiState = ProfileUiState(
            name = "Melissa Velasquez",
            netId = "mv477",
            totalGymDays = 42,
            activeStreak = 5,
            workoutGoal = 4,
            historyItems = listOf(
                HistoryItem(
                    gymName = "Teagle",
                    time = "10:00 AM",
                    date = "March 29, 2024",
                    timestamp = 0L
                )
            ),
            daysOfMonth = listOf(10, 11, 12, 13, 14, 15, 16),
            completedDays = listOf(true, false, true, false, true, false, false),
            workoutsCompleted = 3
        ),
        {},
        {},
        {}
    )
}