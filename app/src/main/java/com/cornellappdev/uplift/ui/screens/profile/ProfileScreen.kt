package com.cornellappdev.uplift.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.profile.GoalsSection
import com.cornellappdev.uplift.ui.components.profile.HistoryItem
import com.cornellappdev.uplift.ui.components.profile.HistorySection
import com.cornellappdev.uplift.ui.components.profile.ProfileHeaderSection
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(){
    /* TODO: Replace with call to viewmodel */
    val name = "John Doe"
    /* TODO: Replace with call to viewmodel */
    val totalWorkouts = 132
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
    val historyItems = listOf(
        HistoryItem("Morrison", "11:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Noyes", "1:00 PM","Fri", "March 29, 2024"),
        HistoryItem("Teagle Up", "2:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Teagle Down", "12:00 PM", "Fri", "March 29, 2024"),
        HistoryItem("Helen Newman", "10:00 AM", "Fri", "March 29, 2024"),
    )

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
                .padding(
                top = innerPadding.calculateTopPadding() + 24.dp,
                start = 16.dp,
                end = 16.dp,
            )
        ){
            /* TODO: Replace {} with viewmodel nav call */
            ProfileHeaderSection(
                name = name,
                totalWorkouts = totalWorkouts,
                navigateToFavorites = {},
                profilePicture
            )
            /* TODO: Replace {} with viewmodel nav call */
            GoalsSection(
                workoutsCompleted = workoutsCompleted,
                workoutGoal = workoutGoal,
                daysOfMonth = daysOfMonth,
                completedDays = completedDays,
                onClick = {}
            )
            /* TODO: Replace {} with viewmodel nav call */
            HistorySection(
                historyItems = historyItems,
                onClick = {}
            )
        }
    }
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
private fun ProfileScreenPreview(){
    ProfileScreen()
}