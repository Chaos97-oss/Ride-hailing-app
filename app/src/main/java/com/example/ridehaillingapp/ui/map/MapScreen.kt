package com.example.ridehaillingapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    isLocationPermissionGranted: Boolean,
    onPickupAddressDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        currentLatLng = latLng

                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                            )
                        }


                        Geocoder(context).getFromLocation(
                            latLng.latitude, latLng.longitude, 1
                        ) { addresses ->
                            if (!addresses.isNullOrEmpty()) {
                                onPickupAddressDetected(addresses[0].getAddressLine(0) ?: "")
                            }
                        }
                    }
                }
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = currentLatLng),
            title = "You are here"
        )
    }
}