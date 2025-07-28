package com.example.ridehaillingapp.ui.map

import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
    onPickupAddressDetected: (String) -> Unit,
    onCurrentLatLngDetected: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }
    val polylinePoints = remember { mutableStateListOf<LatLng>() }


    LaunchedEffect(destinationText) {
        if (destinationText.isNotBlank()) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addressList = geocoder.getFromLocationName(destinationText, 1)
                if (!addressList.isNullOrEmpty()) {
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    destinationLatLng = latLng

                    polylinePoints.clear()
                    currentLocation?.let { polylinePoints.add(it) }
                    polylinePoints.add(latLng)

                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            val locationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location: Location? = locationClient.lastLocation.await()
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

            destinationLatLng?.let {
                Marker(
                    state = MarkerState(position = it),
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