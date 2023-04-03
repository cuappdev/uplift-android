package com.cornellappdev.uplift.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.ui.screens.ClassDetailScreen
import com.cornellappdev.uplift.ui.screens.GymDetailScreen
import com.cornellappdev.uplift.ui.screens.HomeScreen
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The main navigation controller for the app.
 */
@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel = viewModel(),
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        when (destination.route) {
            "home" -> {
                homeViewModel.openHome()
            }
            "classDetail" -> {
                classDetailViewModel.popBackStack()
            }
            "gymDetail" -> {
                gymDetailViewModel.popBackStack()
            }
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen(
                homeViewModel = homeViewModel,
                navController = navController,
                classDetailViewModel = classDetailViewModel,
                gymDetailViewModel = gymDetailViewModel
            )
        }
        composable(route = "gymDetail") {
            GymDetailScreen(
                gymDetailViewModel = gymDetailViewModel,
                navController = navController,
                classDetailViewModel = classDetailViewModel
            ) {
                navController.popBackStack()
            }
        }
        composable(route = "classDetail") {
            ClassDetailScreen(
                classDetailViewModel = classDetailViewModel,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    }
}