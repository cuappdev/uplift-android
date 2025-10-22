package com.cornellappdev.uplift

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.LockScreenOrientation
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

lateinit var datastoreRepository : DatastoreRepository

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var injectedDatastoreRepository: DatastoreRepository


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

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and handle your token
            Log.d("FCM", "Current token: $token")
        }
    }
}



