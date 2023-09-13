package com.cornellappdev.uplift.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.ui.screens.subscreens.MainError
import com.cornellappdev.uplift.ui.screens.subscreens.MainLoaded
import com.cornellappdev.uplift.ui.screens.subscreens.MainLoading
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.valentinilk.shimmer.Shimmer

/**
 * The home page of Uplift.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    gymDetailViewModel: GymDetailViewModel,
    loadingShimmer: Shimmer
) {
    val titleText = homeViewModel.titleFlow.collectAsState().value
    val classesState = homeViewModel.classesFlow.collectAsState().value
    val sportsList = homeViewModel.sportsFlow.collectAsState().value
    val gymsState = homeViewModel.gymFlow.collectAsState().value

    // Loaded!
    if (gymsState is ApiResponse.Success && classesState is ApiResponse.Success) {
        val gymsList = gymsState.data
        val classesList = classesState.data
        MainLoaded(
            gymDetailViewModel = gymDetailViewModel,
            classDetailViewModel = classDetailViewModel,
            sportsList = sportsList,
            upliftClasses = classesList,
            gymsList = gymsList,
            navController = navController,
            titleText = titleText
        )
    }
    // Some error
    else if (gymsState == ApiResponse.Error || classesState == ApiResponse.Error) {
        // TODO: Error...
        MainError()
    }
    // At least one is still loading.
    else {
        // TODO: Loading...
        MainLoading(loadingShimmer)
    }
}
