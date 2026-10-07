package com.cornellappdev.uplift.ui.nav

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.screens.onboarding.ProfileCreationScreen
import com.cornellappdev.uplift.ui.screens.onboarding.WorkoutReminderOnboardingScreen
import com.cornellappdev.uplift.ui.viewmodels.onboarding.ProfileCreationViewModel

/** Registers profile onboarding destinations in the existing navigation graph. */
fun NavGraphBuilder.profileOnboardingGraph(navController: NavHostController) {
    composable<UpliftRootRoute.ProfileCreation> {
        ProfileCreationScreen()
    }

    composable<UpliftRootRoute.GoalsOnboarding> { backStackEntry ->
        // Share the photo screen's viewmodel so account creation includes the selected photo.
        val profileCreationEntry = remember(backStackEntry) {
            navController.getBackStackEntry(UpliftRootRoute.ProfileCreation)
        }
        val viewModel: ProfileCreationViewModel = hiltViewModel(profileCreationEntry)
        WorkoutReminderOnboardingScreen(viewModel = viewModel)
    }
}
