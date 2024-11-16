package com.cornellappdev.uplift.ui.screens

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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