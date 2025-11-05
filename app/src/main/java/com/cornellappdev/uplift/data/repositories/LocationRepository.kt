package com.cornellappdev.uplift.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * Collection of all location data for the user.
 */
object LocationRepository {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var callback: LocationCallback? = null

    /**
     * Compose State for UI (home cards can read this .value and recompose on location updates).
     * Null if not yet initialized.
     * */
    private val _currentLocationState: MutableState<Location?> = mutableStateOf(null)
    val currentLocation: State<Location?> = _currentLocationState

    /**
     * StateFLow for ViewModels to be able to collect and react to location updates. Null if not yet
     * initialized.
     */
    private val _currentLocationFlow = MutableStateFlow<Location?>(null)
    val currentLocationFlow: StateFlow<Location?> = _currentLocationFlow

    /**
     * Initializes the fused location client, if hasn't already been.
     */
    fun instantiate(context: Context) {
        if (!::fusedLocationClient.isInitialized){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    /**
     * Updates [Location] every 30 seconds. Also updates [currentLocation] and [currentLocationFlow]
     * to the user's current [Location].
     */
    fun startLocationUpdates(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        if (callback != null) return

        instantiate(context)

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            30_000L
        ).build()

        val cb = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                _currentLocationState.value = locationResult.lastLocation
                _currentLocationFlow.value = locationResult.lastLocation
            }
        }

        callback = cb

        fusedLocationClient.requestLocationUpdates(
            request,
            cb,
            Looper.getMainLooper()
        )
    }

    /**
     * Stops location updates.
     */
    fun stopLocationUpdates() {
        val cb = callback ?: return
        fusedLocationClient.removeLocationUpdates(cb)
        callback = null
    }


}
