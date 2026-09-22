package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.animation.Crossfade
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
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.profile.workouts.GoalsSection
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItem
import com.cornellappdev.uplift.ui.components.profile.workouts.HistorySection
import com.cornellappdev.uplift.ui.components.profile.ProfileHeaderSection
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainError
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileUiState
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun ProfileScreen(
    loadingShimmer: Shimmer,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    ProfileScreenContent(
        uiState = uiState,
        toSettings = viewModel::toSettings,
        toGoals = viewModel::toGoals,
        toHistory = viewModel::toHistory,
        onRetry = viewModel::reload,
        loadingShimmer = loadingShimmer
    )
}

private enum class ProfileContentState { Loading, Error, Loaded }

@Composable
private fun ProfileScreenContent(
    uiState: ProfileUiState,
    toSettings: () -> Unit,
    toGoals: () -> Unit,
    toHistory: () -> Unit,
    onRetry: () -> Unit,
    loadingShimmer: Shimmer
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            ProfileScreenTopBar(navigateToSettings = toSettings)
        }
    ) { innerPadding ->
        // Render placeholders before data arrives instead of flashing empty names and zero stats.
        // Keep the toolbar stable and crossfade on loading/error/content changes.
        val contentState = when {
            uiState.loading -> ProfileContentState.Loading
            uiState.error -> ProfileContentState.Error
            else -> ProfileContentState.Loaded
        }
        Crossfade(
            targetState = contentState,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            label = "Profile"
        ) { state ->
            when (state) {
                ProfileContentState.Loading -> ProfileLoading(loadingShimmer)
                // Reuse the existing retry UI so a failed request does not look like an empty profile.
                ProfileContentState.Error -> MainError(reload = onRetry)
                ProfileContentState.Loaded ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 24.dp,
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
                            historyItems = uiState.historyItems,
                            navigateToGoalsSection = toGoals,
                            navigateToHistorySection = toHistory
                        )

                    }
            }
        }
    }
}

@Composable
private fun WorkoutsSectionContent(
    workoutsCompleted: Int,
    workoutGoal: Int,
    daysOfMonth: List<Int>,
    completedDays: List<Boolean>,
    historyItems: List<HistoryItem>,
    navigateToGoalsSection: () -> Unit,
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
    val now = System.currentTimeMillis()
    val historyItems = listOf(
        HistoryItem("Morrison", "11:00 PM", "March 29, 2024", now, "Today", "Mar 29"),
        HistoryItem("Noyes", "1:00 PM", "March 28, 2024", now - 86400000L, "Yesterday", "Mar 28"),
        HistoryItem("Teagle Up", "2:00 PM", "February 15, 2024", now - 4000000000L, "1 month ago", "Feb 15"),
        HistoryItem("Helen Newman", "9:30 AM", "February 10, 2024", now - 4430000000L, "1 month ago", "Feb 10"),
        HistoryItem("Morrison", "6:45 PM", "February 3, 2024", now - 5030000000L, "1 month ago", "Feb 3")
    )
    ProfileScreenContent(
        uiState = ProfileUiState(
            name = "Melissa Velasquez",
            netId = "mv477",
            totalGymDays = 1,
            activeStreak = 0,
            workoutGoal = 4,
            historyItems = historyItems,
            daysOfMonth = listOf(10, 11, 12, 13, 14, 15, 16),
            completedDays = listOf(true, false, false, false, false, false, false),
            workoutsCompleted = 1
        ),
        {},
        {},
        {},
        onRetry = {},
        loadingShimmer = rememberShimmer(ShimmerBounds.Window)
    )
}