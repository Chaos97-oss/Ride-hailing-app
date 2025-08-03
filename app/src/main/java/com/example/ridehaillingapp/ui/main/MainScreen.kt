package com.example.ridehaillingapp.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ridehaillingapp.data.model.Driver
import com.example.ridehaillingapp.data.model.LocationData
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.ui.map.MapScreen
import androidx.compose.ui.text.input.TextFieldValue
import com.example.ridehaillingapp.viewmodel.RideViewModel
import com.google.android.gms.maps.model.LatLng

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(
    navController: NavHostController,
    isLocationPermissionGranted: Boolean,
    viewModel: RideViewModel = hiltViewModel()
) {
    val rides by viewModel.rides.collectAsState()
    val fareEstimate by viewModel.fareEstimate.collectAsState()
    val rideConfirmation by viewModel.rideConfirmation.collectAsState()

    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var destinationLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
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


        MapScreen(
            isLocationPermissionGranted = isLocationPermissionGranted,
            destinationText = destination,
            onPickupAddressDetected = { detectedAddress ->
                pickup = detectedAddress
            },
            onCurrentLatLngDetected = { latLng ->
                currentLatLng = latLng
            },
            onDestinationLatLngDetected = { latLng ->
                destinationLatLng = latLng
            },
            destinationLatLng = destinationLatLng,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Request a Ride", style = MaterialTheme.typography.headlineSmall)

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
                            name = rideConfirmation?.driverName ?: "John Doe",
                            rating = 4.5f,
                            licenseNumber = rideConfirmation?.plateNumber ?: "N/A"
                        ),
                        startLocation = LocationData(
                            latitude = currentLatLng.latitude,
                            longitude = currentLatLng.longitude,
                            address = pickup
                        ),
                        endLocation = LocationData(
                            latitude = destinationLatLng.latitude,
                            longitude = destinationLatLng.longitude,
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

        Text("Estimated Fare: ${fareEstimate?.let { "$%.2f".format(it) } ?: "--"}")
        Text("Ride Status: ${rideConfirmation?.status ?: "Not Requested"}")

        Spacer(modifier = Modifier.height(16.dp))
        Text("Ride History", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
        ) {
            items(rides) { ride ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
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