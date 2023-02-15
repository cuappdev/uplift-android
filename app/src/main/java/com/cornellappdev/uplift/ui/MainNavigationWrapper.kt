package com.cornellappdev.uplift.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.ui.screens.GymDetailScreen
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel

@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "gymDetail") {
        composable(route = "gymDetail") {
            GymDetailScreen(gymDetailViewModel = gymDetailViewModel)
        }
    }
}