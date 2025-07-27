package com.example.ridehaillingapp.ui.map

import android.annotation.SuppressLint
import android.content.Context
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    isLocationPermissionGranted: Boolean,
    modifier: Modifier = Modifier,
    onPickupAddressDetected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }
    var destinationText by remember { mutableStateOf(TextFieldValue("")) }

    val cameraPositionState = rememberCameraPositionState()
    val mapProperties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted)
    val mapUiSettings = MapUiSettings(zoomControlsEnabled = false)


    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            try {
                val location: Location? = fusedLocationProviderClient.lastLocation.await()
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    userLocation = currentLatLng
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    val address = getAddressFromLatLng(context, currentLatLng)
                    onPickupAddressDetected(address)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    LaunchedEffect(destinationText.text) {
        if (destinationText.text.isNotEmpty()) {
            try {
                val geocoder = Geocoder(context)
                val result = withContext(Dispatchers.IO) {
                    geocoder.getFromLocationName(destinationText.text, 1)
                }
                if (!result.isNullOrEmpty()) {
                    val location = result[0]
                    destinationLatLng = LatLng(location.latitude, location.longitude)
                }
            } catch (e: Exception) {
                destinationLatLng = null
            }
        }
    }


    Column(modifier = modifier.fillMaxSize()) {

//        OutlinedTextField(
//            value = destinationText,
//            onValueChange = { destinationText = it },
//            label = { Text("Enter Destination") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        )

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
            userLocation?.let { current ->
                Marker(
                    state = MarkerState(position = current),
                    title = "Pickup Location",
                    snippet = "You're here"
                )
            }

            destinationLatLng?.let { destination ->
                Marker(
                    state = MarkerState(position = destination),
                    title = "Destination"
                )

                userLocation?.let { pickup ->
                    Polyline(
                        points = listOf(pickup, destination),
                        color = Color.Blue,
                        width = 5f
                    )
                }
            }
        }
    }
}

private suspend fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = withContext(Dispatchers.IO) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        }
        addressList?.firstOrNull()?.getAddressLine(0) ?: "Unknown address"
    } catch (e: Exception) {
        "Unknown address"
    }
}