package com.cornellappdev.uplift.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * Collection of all location data for the user.
 */
object LocationRepository {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val _currentLocation: MutableState<Location?> = mutableStateOf(null)

    /**
     * Either is the current user's location, or null if the location has not yet
     * been initialized.
     * */
    var currentLocation = (_currentLocation as State<Location?>)

    /**
     * Starts updating [currentLocation] to the user's current location.
     */
    fun instantiate(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        updateLocation(context)
    }


    private fun updateLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            _currentLocation.value = it
        }
    }
}
