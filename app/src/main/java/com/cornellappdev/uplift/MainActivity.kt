package com.cornellappdev.uplift

import android.Manifest
import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.LockScreenOrientation
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

lateinit var datastoreRepository : DatastoreRepository

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var injectedDatastoreRepository: DatastoreRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            storeFCMToken()
        } else {
            showNotificationPermissionRationale()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        datastoreRepository = injectedDatastoreRepository


        checkNotificationPermission()

        setContent {
            UpliftTheme {
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                MainNavigationWrapper()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    storeFCMToken()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    showNotificationPermissionRationale()
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            storeFCMToken()
        }
    }

    // TODO: Ask design for a permission rationale dialog design and replace
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotificationPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Notifications Permission")
            .setMessage("Uplift needs notification permission to keep you updated about gym capacities, workout reminders, and important announcements. Would you like to enable notifications?")
            .setPositiveButton("Yes") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("No Thanks") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()

    }

    private fun storeFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                CoroutineScope(Dispatchers.IO).launch {
                    datastoreRepository.storePreference(
                        key = PreferencesKeys.FCM_TOKEN,
                        value = task.result
                    )
                }
            }
        }
    }
}



