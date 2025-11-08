package com.cornellappdev.uplift

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.ui.viewmodels.profile.CheckInViewModel
import com.cornellappdev.uplift.util.LockScreenOrientation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

lateinit var datastoreRepository : DatastoreRepository

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var injectedDatastoreRepository: DatastoreRepository


    private val checkInViewModel: CheckInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        datastoreRepository = injectedDatastoreRepository

        setContent {
            UpliftTheme {
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                MainNavigationWrapper()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkInViewModel.startLocationUpdates(this)
    }

    override fun onPause() {
        super.onPause()
        checkInViewModel.stopLocationUpdates()
    }
}
