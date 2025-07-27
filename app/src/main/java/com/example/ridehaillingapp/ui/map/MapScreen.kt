package com.example.ridehaillingapp.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    isLocationPermissionGranted: Boolean,
    onPickupAddressDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState()
    val mapProperties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted)
    val mapUiSettings = MapUiSettings(zoomControlsEnabled = false)

    GoogleMap(
        modifier = modifier,
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            destinationLatLng = latLng
        },
        onMapLoaded = {
            userLocation = LatLng(6.5244, 3.3792) // Lagos coordinates
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
            detectAddress(context, userLocation) { address ->
                onPickupAddressDetected(address)
            }
        }
    ) {

        Marker(
            state = MarkerState(position = userLocation),
            title = "You",
            snippet = "Current Location"
        )


        destinationLatLng?.let { dest ->
            Marker(
                state = MarkerState(position = dest),
                title = "Destination"
            )

            Polyline(
                points = listOf(userLocation, dest),
                color = Color.Blue, // ✅ Jetpack Compose Color
                width = 5f
            )
        }
    }
}

private fun detectAddress(
    context: Context,
    latLng: LatLng,
    onResult: (String) -> Unit
) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (!address.isNullOrEmpty()) {
            onResult(address[0].getAddressLine(0) ?: "Unknown Address")
        } else {
            onResult("Unknown Address")
        }
    } catch (e: Exception) {
        onResult("Unknown Address")
    }
}