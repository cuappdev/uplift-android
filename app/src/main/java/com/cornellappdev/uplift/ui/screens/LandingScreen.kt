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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.R
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The landing page during the Uplift onboarding process.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LandingScreen() {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_main_logo),
            contentDescription = "Uplift logo",
            modifier = Modifier
                .offset(y = (-100).dp)
                .height(130.dp)
                .width(115.dp)
                .align(Alignment.Center)
        )

        Spacer(modifier = Modifier.height(387.dp));

        Image(
            painter = painterResource(id = R.drawable.ic_appdev_logo),
            contentDescription = "Cornell AppDev logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .paddingFromBaseline(bottom = 80.dp)
                .height(26.dp)
                .width(158.dp)

        )
    }
}