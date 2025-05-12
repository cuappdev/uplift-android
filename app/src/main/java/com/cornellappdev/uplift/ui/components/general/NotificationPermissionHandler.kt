package com.cornellappdev.uplift.ui.components.general

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.ui.viewmodels.notifications.NotificationPermissionViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A composable that handles the notification permission request flow that can be used in any screen that requires notification permissions
 */
@Composable
fun NotificationPermissionHandler(
    notificationPermissionViewModel: NotificationPermissionViewModel = hiltViewModel(),
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
) {
    val context = LocalContext.current
    val permissionState = notificationPermissionViewModel.collectUiStateValue().permissionState

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationPermissionViewModel.onPermissionResult(isGranted)
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        notificationPermissionViewModel.checkNotificationPermission(context)
    }

    when (permissionState) {
        is NotificationPermissionViewModel.PermissionState.ShowRationale -> {
            NotificationRationaleDialog(
                onConfirm = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
                onDismiss = {
                    notificationPermissionViewModel.onPermissionResult(false)
                    onPermissionDenied()
                }
            )
        }

        is NotificationPermissionViewModel.PermissionState.Granted -> {
            LaunchedEffect(Unit) {
                onPermissionGranted()
            }
        }

        else -> return
    }
}

@Composable
private fun NotificationRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Notifications Permission",
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Text(
                "Uplift needs notification permission to keep you updated about gym capacities, workout reminders, and important announcements. Would you like to enable notifications?",
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium

            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    "Yes",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PRIMARY_BLACK
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "No Thanks",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PRIMARY_BLACK
                )
            }
        },
        containerColor = Color.White,

        )
}