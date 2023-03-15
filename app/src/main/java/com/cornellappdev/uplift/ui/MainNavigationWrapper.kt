package com.cornellappdev.uplift.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.ui.screens.ClassDetailScreen
import com.cornellappdev.uplift.ui.screens.GymDetailScreen
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.exampleClassMusclePump1
import com.cornellappdev.uplift.util.testMorrison
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The main navigation controller for the app.
 */
@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel = viewModel(),
    classDetailViewModel: ClassDetailViewModel = viewModel()
) {
    val navController = rememberNavController()
    val systemUiController: SystemUiController = rememberSystemUiController()

    // Just for testing...
    gymDetailViewModel.selectGym(testMorrison)
    classDetailViewModel.selectClass(exampleClassMusclePump1)

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    NavHost(navController = navController, startDestination = "classDetail") {
        composable(route = "gymDetail") {
            GymDetailScreen(gymDetailViewModel = gymDetailViewModel)
        }
        composable(route = "classDetail") {
            ClassDetailScreen(classDetailViewModel = classDetailViewModel)
        }
    }
}