package com.example.ridehaillingapp.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ridehaillingapp.data.model.Driver
import com.example.ridehaillingapp.data.model.LocationData
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.viewmodel.RideViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: RideViewModel = hiltViewModel()
) {
    val rides by viewModel.rides.collectAsState()
    val fareEstimate by viewModel.fareEstimate.collectAsState()
    val rideConfirmation by viewModel.rideConfirmation.collectAsState()

    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
    }


    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return@LaunchedEffect
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                currentLatLng = latLng


                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)


                Geocoder(context).getFromLocation(it.latitude, it.longitude, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        pickup = addresses[0].getAddressLine(0) ?: ""
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Ride History")
        }

        Spacer(modifier = Modifier.height(8.dp))

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = currentLatLng),
                title = "You are here"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Request a Ride",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = pickup,
            onValueChange = { pickup = it },
            label = { Text("Pickup Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val distanceKm = (1..10).random().toDouble()
                    val timeMinutes = (5..20).random().toDouble()
                    viewModel.calculateFare(distanceKm, timeMinutes)
                }
            ) {
                Text("Estimate Fare")
            }

            Button(
                onClick = {
                    viewModel.requestRide()

                    val ride = Ride(
                        driver = Driver(
                            name = rideConfirmation?.driverName ?: "Unknown",
                            rating = 4.5f,
                            licenseNumber = rideConfirmation?.plateNumber ?: "N/A"
                        ),
                        startLocation = LocationData(
                            latitude = currentLatLng.latitude,
                            longitude = currentLatLng.longitude,
                            address = pickup
                        ),
                        endLocation = LocationData(
                            latitude = 0.0,
                            longitude = 0.0,
                            address = destination
                        ),
                        fare = fareEstimate ?: 0.0,
                        timestamp = System.currentTimeMillis()
                    )
                    viewModel.addRide(ride)
                }
            ) {
                Text("Request Ride")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estimated Fare: ${fareEstimate?.let { "$%.2f".format(it) } ?: "--"}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Ride Status: ${rideConfirmation?.status ?: "Not Requested"}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ride History",
            style = MaterialTheme.typography.headlineSmall
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
        ) {
            items(rides) { ride ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Driver: ${ride.driver.name}")
                        Text("From: ${ride.startLocation.address}")
                        Text("To: ${ride.endLocation.address}")
                        Text("Fare: $${"%.2f".format(ride.fare)}")
                    }
                }
            }
        }
    }
}