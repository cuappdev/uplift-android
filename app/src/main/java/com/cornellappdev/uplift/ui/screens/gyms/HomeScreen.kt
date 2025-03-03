package com.cornellappdev.uplift.ui.screens.gyms

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.data.repositories.LocationRepository
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainError
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainLoaded
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainLoading
import com.cornellappdev.uplift.ui.viewmodels.gyms.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.gyms.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.delay

/**
 * The home page of Uplift.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    gymDetailViewModel: GymDetailViewModel,
    loadingShimmer: Shimmer
) {
    val homeUiState = homeViewModel.collectUiStateValue()
    val titleText = homeUiState.title
    val gymsState = homeUiState.gyms
    val gymsLoading = homeUiState.gymsLoading
    val gymsError = homeUiState.gymsError
    var showCapacities by remember { mutableStateOf(false) }

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

    Crossfade(targetState = gymsState, label = "Main") {
        when {
            gymsLoading -> MainLoading(loadingShimmer)
            gymsError -> MainError(reload = homeViewModel::reload)
            gymsState.isNotEmpty() -> MainLoaded(
                gymDetailViewModel = gymDetailViewModel,
                gymsList = gymsState,
                navController = navController,
                showCapacities = showCapacities,
                titleText = titleText,
                onToggleCapacities = { showCapacities = !showCapacities },
                reload = homeViewModel::reload,
            )
        }
    }
}
