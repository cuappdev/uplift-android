package com.cornellappdev.uplift.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTabRow
import com.cornellappdev.uplift.ui.components.profile.GoalsSection
import com.cornellappdev.uplift.ui.components.profile.HistoryItem
import com.cornellappdev.uplift.ui.components.profile.HistorySection
import com.cornellappdev.uplift.ui.components.profile.MyRemindersSection
import com.cornellappdev.uplift.ui.components.profile.ProfileHeaderSection
import com.cornellappdev.uplift.ui.components.profile.ReminderItem
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    /* TODO: Replace with call to viewmodel */
    val name = "John Doe"
    /* TODO: Replace with call to viewmodel */
    val gymDays = 132
    /* TODO: Replace with call to viewmodel */
    val streaks = 14
    /* TODO: Replace with call to viewmodel */
    val badges = 6
    /* TODO: Replace with call to viewmodel */
    val profilePicture = null
    /* TODO: Replace with call to viewmodel */
    val workoutsCompleted = 3
    /* TODO: Replace with call to viewmodel */
    val workoutGoal = 5
    /* TODO: Replace with call to viewmodel */
    val daysOfMonth = (25..31).toList()
    /* TODO: Replace with call to viewmodel */
    val completedDays = listOf(false, true, true, false, true, false, false)
    /* TODO: Replace with call to viewmodel */
    val reminderItems = listOf(
        ReminderItem("Mon", "8:00 AM", "9:00 AM", true),
        ReminderItem("Tue", "8:00 AM", "12:00 PM", false),
        ReminderItem("Wed", "8:00 AM", "9:00 AM", true),
        ReminderItem("Thu", "8:00 AM", "9:00 AM", false),
        ReminderItem("Fri", "11:30 AM", "12:00 PM", true),
    )
    /* TODO: Replace with call to viewmodel */
    val historyItems = listOf(
        HistoryItem("Morrison", "11:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Noyes", "1:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Teagle Up", "2:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Teagle Down", "12:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Helen Newman", "10:00 AM", "Fri", "March 29, 2024"),
    )

    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("WORKOUTS", "ACHIEVEMENTS")

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            /* TODO: Replace {} with viewmodel nav call */
            ProfileScreenTopBar(navigateToSettings = {})
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    top = innerPadding.calculateTopPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
        ) {
            /* TODO: Replace {} with viewmodel function call */
            ProfileHeaderSection(
                name = name,
                gymDays = gymDays,
                streaks = streaks,
                badges = badges,
                profilePictureUri = profilePicture,
                onPhotoSelected = {}
            )
            UpliftTabRow(tabIndex, tabs, onTabChange = { tabIndex = it })
            when (tabIndex) {
                0 -> WorkoutsSectionContent(
                    workoutsCompleted,
                    workoutGoal,
                    daysOfMonth,
                    completedDays,
                    reminderItems,
                    historyItems,
                    navigateToGoalsSection = { /* TODO: Replace {} with viewmodel nav call */ },
                    navigateToRemindersSection = { /* TODO: Replace {} with viewmodel nav call */ },
                    navigateToHistorySection = { /* TODO: Replace {} with viewmodel nav call */ }
                )

                1 -> AchievementsSectionContent()
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
    reminderItems: List<ReminderItem>,
    historyItems: List<HistoryItem>,
    navigateToGoalsSection: () -> Unit,
    navigateToRemindersSection: () -> Unit,
    navigateToHistorySection: () -> Unit
) {
    GoalsSection(
        workoutsCompleted = workoutsCompleted,
        workoutGoal = workoutGoal,
        daysOfMonth = daysOfMonth,
        completedDays = completedDays,
        onClick = navigateToGoalsSection,
    )
    MyRemindersSection(
        reminderItems,
        onClickHeader = navigateToRemindersSection,
    )
    HistorySection(
        historyItems = historyItems,
        onClick = navigateToHistorySection,
    )
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

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}