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
import com.example.ridehaillingapp.data.model.Driver
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.model.LocationData
import com.example.ridehaillingapp.viewmodel.RideViewModel
import com.example.ridehaillingapp.util.FareCalculator
import kotlin.random.Random

@Composable
fun MainScreen(
    viewModel: RideViewModel = hiltViewModel()
) {
    val rides by viewModel.rides.collectAsState()

    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var fareEstimate by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Request a Ride",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
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

        Button(
            onClick = {
                val distanceKm = Random.nextInt(1, 10).toDouble()
                val isPeakHour = false  // Simulate peak hours if needed
                val trafficLevel = 1.2  // Simulated traffic

                fareEstimate = FareCalculator.calculateFare(distanceKm, isPeakHour, trafficLevel)

                val ride = Ride(
                    driver = Driver(
                        name = "John Doe",
                        rating = 4.5f,
                        licenseNumber = "XYZ1234"
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
                    fare = fareEstimate,
                    timestamp = System.currentTimeMillis()
                )

                viewModel.addRide(ride)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Request Ride")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estimated Fare: $${"%.2f".format(fareEstimate)}",
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