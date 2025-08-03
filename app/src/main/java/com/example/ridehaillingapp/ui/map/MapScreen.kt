package com.example.ridehaillingapp.ui.map

import android.Manifest
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ridehaillingapp.util.decodePolyline
import androidx.compose.ui.unit.dp
import com.example.ridehaillingapp.BuildConfig
import com.example.ridehaillingapp.data.api.RetrofitClient
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    isLocationPermissionGranted: Boolean,
    destinationText: String,
    destinationLatLng: LatLng,
    onPickupAddressDetected: (String) -> Unit,
    onCurrentLatLngDetected: (LatLng) -> Unit,
    onDestinationLatLngDetected: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val polylinePoints = remember { mutableStateListOf<LatLng>() }


    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            val locationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location = locationClient.lastLocation.await()
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    currentLocation = userLatLng
                    polylinePoints.clear()
                    polylinePoints.add(userLatLng)
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                    onCurrentLatLngDetected(userLatLng)

                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addressList = geocoder.getFromLocation(userLatLng.latitude, userLatLng.longitude, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val pickupAddress = addressList[0].getAddressLine(0)
                        onPickupAddressDetected(pickupAddress)
                    }
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }


    LaunchedEffect(destinationText) {
        if (destinationText.isNotBlank()) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addressList = geocoder.getFromLocationName(destinationText, 1)
                if (!addressList.isNullOrEmpty()) {
                    val address = addressList[0]
                    val destination = LatLng(address.latitude, address.longitude)
                    onDestinationLatLngDetected(destination)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    LaunchedEffect(destinationLatLng, currentLocation) {
        Log.d("PolylineDebug", "destination: $destinationLatLng")
        Log.d("PolylineDebug", "currentLocation: $currentLocation")

        if (destinationLatLng.latitude != 0.0 && currentLocation != null) {
            try {
                val response = RetrofitClient.api.getDirections(
                    origin = "${currentLocation!!.latitude},${currentLocation!!.longitude}",
                    destination = "${destinationLatLng.latitude},${destinationLatLng.longitude}",
                    apiKey = BuildConfig.MAPS_API_KEY
                )

                if (response.routes.isNotEmpty()) {
                    val encoded = response.routes[0].overview_polyline.points
                    val polylinePath = decodePolyline(encoded)
                    polylinePoints.clear()
                    polylinePoints.addAll(polylinePath)
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(destinationLatLng))
                } else {
                    Log.e("PolylineDebug", "No routes returned from Directions API")
                    Log.e("PolylineDebug", "Origin: ${currentLocation!!.latitude},${currentLocation!!.longitude}")
                    Log.e("PolylineDebug", "Destination: ${destinationLatLng.latitude},${destinationLatLng.longitude}")
                    Toast.makeText(context, "No route found between points", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("PolylineDebug", "Error fetching directions", e)
                Toast.makeText(context, "Failed to get route", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // UI
    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        ) {
            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Pickup Location",
                    snippet = "You are here"
                )
            }

            if (destinationLatLng.latitude != 0.0 && destinationLatLng.longitude != 0.0) {
                Marker(
                    state = MarkerState(position = destinationLatLng),
                    title = "Destination"
                )
            }

            if (polylinePoints.size >= 2) {
                Polyline(
                    points = polylinePoints.toList(),
                    color = Color.Blue,
                    width = 5f
                )
            }
        }
    }
}