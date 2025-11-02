package com.cornellappdev.uplift.ui.screens.gyms

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.repositories.LocationRepository
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityReminderTutorial
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainError
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainLoaded
import com.cornellappdev.uplift.ui.screens.gyms.subscreens.MainLoading
import com.cornellappdev.uplift.ui.viewmodels.gyms.HomeViewModel
import com.cornellappdev.uplift.util.GRAY04
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.delay

/**
 * The home page of Uplift.
 */
@SuppressLint("UnusedCrossfadeTargetStateParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
    openGym: (UpliftGym) -> Unit,
    loadingShimmer: Shimmer
) {
    val homeUiState = homeViewModel.collectUiStateValue()
    val titleText = homeUiState.title
    val gymsState = homeUiState.gyms
    val gymsLoading = homeUiState.gymsLoading
    val gymsError = homeUiState.gymsError
    var showCapacities by remember { mutableStateOf(false) }
    val showTutorial: Boolean by homeViewModel.showTutorial.collectAsState()

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

    Box(modifier = Modifier.fillMaxSize()){
        Crossfade(targetState = gymsState, label = "Main") {
            when {
                gymsLoading -> MainLoading(loadingShimmer)
                gymsError -> MainError(reload = homeViewModel::reload)
                gymsState.isNotEmpty() -> MainLoaded(
                    openGym = openGym,
                    gymsList = gymsState,
                    navController = navController,
                    showCapacities = showCapacities,
                    titleText = titleText,
                    onToggleCapacities = { showCapacities = !showCapacities },
                    reload = homeViewModel::reload,
                    navigateToCapacityReminders = homeViewModel::navigateToCapacityReminders
                )
            }
        }

        if(showTutorial) {
            Box(modifier = Modifier.fillMaxSize().background(GRAY04.copy(alpha=0.5f))){
                Box(modifier = Modifier.align(Alignment.Center)) {
                    CapacityReminderTutorial(
                        onAccept = { homeViewModel.navigateToCapacityReminders() },
                        onDismiss = { homeViewModel.onTutorialDismissed() },
                    )
                }
            }
        }
    }
}
