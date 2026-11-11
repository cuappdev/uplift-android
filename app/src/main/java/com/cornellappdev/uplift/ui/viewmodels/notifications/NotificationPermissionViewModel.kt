package com.cornellappdev.uplift.ui.viewmodels.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.DatastoreRepository
import com.cornellappdev.uplift.data.repositories.PreferencesKeys
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationPermissionViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) : UpliftViewModel<NotificationPermissionViewModel.NotificationPermissionUiState>(
    NotificationPermissionUiState()
) {

    sealed class PermissionState {
        object Loading : PermissionState()
        object Granted : PermissionState()
        object ShowRationale : PermissionState()
        object Denied : PermissionState()
    }

    data class NotificationPermissionUiState(
        val permissionState: PermissionState = PermissionState.Loading
    )

    fun checkNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModelScope.launch {
                val hasDeclinedPermission = datastoreRepository.getPreference(
                    PreferencesKeys.DECLINED_NOTIFICATION_PERMISSION
                ) == true

                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        applyMutation {
                            copy(
                                permissionState = PermissionState.Granted
                            )
                        }
                        storeFCMToken()
                    }

                    hasDeclinedPermission -> {
                        applyMutation {
                            copy(
                                permissionState = PermissionState.Denied
                            )
                        }
                    }

                    else -> {
                        applyMutation {
                            copy(
                                permissionState = PermissionState.ShowRationale
                            )
                        }
                    }
                }
            }
        } else {
            applyMutation {
                copy(
                    permissionState = PermissionState.Granted
                )
            }
            storeFCMToken()
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            applyMutation {
                copy(
                    permissionState = PermissionState.Granted
                )
            }
            storeFCMToken()
        } else {
            viewModelScope.launch {
                datastoreRepository.storePreference(
                    key = PreferencesKeys.DECLINED_NOTIFICATION_PERMISSION,
                    value = true
                )
                applyMutation {
                    copy(
                        permissionState = PermissionState.Denied
                    )
                }
            }
        }
    }

    private fun storeFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    datastoreRepository.storePreference(
                        key = PreferencesKeys.FCM_TOKEN,
                        value = task.result
                    )
                }
            }
        }
    }
}