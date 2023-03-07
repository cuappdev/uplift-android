package com.cornellappdev.uplift.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.ui.screens.GymDetailScreen
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The main navigation controller for the app.
 */
@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel
) {
    val navController = rememberNavController()
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    NavHost(navController = navController, startDestination = "gymDetail") {
        composable(route = "gymDetail") {
            GymDetailScreen(gymDetailViewModel = gymDetailViewModel)
        }
    }
}