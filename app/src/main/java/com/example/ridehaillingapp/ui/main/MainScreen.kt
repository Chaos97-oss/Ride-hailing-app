package com.example.ridehaillingapp.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ridehaillingapp.data.model.Driver
import com.example.ridehaillingapp.data.model.LocationData
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.viewmodel.RideViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: RideViewModel = hiltViewModel()
) {
    val rides by viewModel.rides.collectAsState()
    val fareEstimate by viewModel.fareEstimate.collectAsState(initial = null)
    val rideConfirmation by viewModel.rideConfirmation.collectAsState(initial = null)

    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 12f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Request a Ride",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = { navController.navigate("history") }) {
                Text("View History")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = LatLng(0.0, 0.0)),
                title = "You are here"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
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
                            latitude = 0.0,
                            longitude = 0.0,
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