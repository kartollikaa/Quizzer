package com.kartollika.quizzer.data.datasource

import android.annotation.SuppressLint
import android.location.LocationListener
import android.location.LocationManager
import com.kartollika.quizzer.domain.datasource.LocationDataSource
import com.kartollika.quizzer.domain.model.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
  private val locationManager: LocationManager
) : LocationDataSource {

  override fun locationEnabled(): Boolean {
    return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
  }

  @SuppressLint("MissingPermission")
  private val _locationUpdates = callbackFlow {
    val callback =
      LocationListener { location -> trySend(Location(location.latitude, location.longitude)) }

    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5f, callback)

    awaitClose {
      locationManager.removeUpdates(callback)
    }
  }

  override fun getLocations(): Flow<Location> {
    return _locationUpdates
  }

  @SuppressLint("MissingPermission")
  override fun getLastKnownLocation(): Flow<Location?> = callbackFlow {
    val callback =
      LocationListener { location ->
        trySend(Location(location.latitude, location.longitude))
      }

    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10f, callback)

    awaitClose {
      locationManager.removeUpdates(callback)
    }
  }
}