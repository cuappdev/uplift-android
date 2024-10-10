package com.cornellappdev.uplift

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import com.cornellappdev.uplift.models.DatastoreRepository
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.screens.ProfileCreationScreen
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.LockScreenOrientation
import com.cornellappdev.uplift.util.PREFERENCES_NAME

// Singleton
lateinit var datastoreRepository: DatastoreRepository

class MainActivity : ComponentActivity() {
    val Context.dataStore by preferencesDataStore(
        name = PREFERENCES_NAME
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        // If statement needed to fix crash when datastore initializes twice.
        if (!::datastoreRepository.isInitialized)
            datastoreRepository = DatastoreRepository(dataStore)

        setContent {
//            UpliftTheme {
//                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                MainNavigationWrapper()
//            }
            ProfileCreationScreen()
        }
    }
}
