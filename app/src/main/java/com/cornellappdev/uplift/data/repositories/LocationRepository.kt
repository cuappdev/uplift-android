package com.cornellappdev.uplift.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

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
    }


    /**
     * A [Flow] emitting the most recent [Location] every 30 seconds, or `null` if unavailable. ALso
     * updates [currentLocation] to the most recent [Location].
     */
    fun locationFlow(context: Context): Flow<Location?> = flow {
        while (true) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val location = try {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                } catch(e: Exception) {
                    Log.e("LocationRepo", "Failed to get location", e)
                    null
                }

                if (location != null){
                    _currentLocation.value = location
                    emit(location)
                } else {
                    Log.d("LocationRepo", "No location available right now")
                }
            } else {
                Log.d("LocationRepo", "Location permissions not granted")
            }
            delay(30_000L)
        }
    }
}
