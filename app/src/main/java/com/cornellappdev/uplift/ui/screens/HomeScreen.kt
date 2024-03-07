package com.cornellappdev.uplift.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.models.LocationRepository
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.ui.screens.subscreens.MainError
import com.cornellappdev.uplift.ui.screens.subscreens.MainLoaded
import com.cornellappdev.uplift.ui.screens.subscreens.MainLoading
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.delay
import android.util.Log
/**
 * The home page of Uplift.
 */
@OptIn(ExperimentalPermissionsApi::class)
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
    val gymsState = homeViewModel.gymFlow.collectAsState().value
    val showCapacities by homeViewModel.showCapacities

    val context = LocalContext.current

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(true) {
        delay(1500L)
        locationPermissionState.launchMultiplePermissionRequest()
    }

    if (locationPermissionState.allPermissionsGranted) {
        LocationRepository.instantiate(context)
    }

    Crossfade(targetState = Pair(gymsState, classesState), label = "Main") { state ->
        val gState = state.first
        val cState = state.second

        // Loaded!
        if (gState is ApiResponse.Success && cState is ApiResponse.Success) {
            val gymsList = gState.data
            val classesList = cState.data
            MainLoaded(
                gymDetailViewModel = gymDetailViewModel,
                classDetailViewModel = classDetailViewModel,
                upliftClasses = classesList,
                gymsList = gymsList,
                navController = navController,
                showCapacities = showCapacities,
                titleText = titleText
            ) {
                homeViewModel.toggleCapacities()
            }
        }
        // Some error
        else if (gState == ApiResponse.Error || cState == ApiResponse.Error) {
            MainError()
        }
        // At least one is still loading.
        else {
            MainLoading(loadingShimmer)
        }
    }
}
