package com.example.ridehaillingapp.ui.ridelist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ridehaillingapp.data.model.Driver
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.data.model.LocationData
import com.example.ridehaillingapp.viewmodel.RideViewModel
import kotlin.random.Random

@Composable
fun RideListScreen(
    viewModel: RideViewModel = hiltViewModel()
) {
    val rides = viewModel.rides.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.addRide(
                        Ride(
                            id = Random.nextInt(1, Int.MAX_VALUE),
                            driver = Driver(
                                name = "John Doe",
                                rating = 4.5f,
                                licenseNumber = "ABC123"
                            ),
                            startLocation = LocationData(
                                latitude = 6.5244,
                                longitude = 3.3792,
                                address = "Lagos"
                            ),
                            endLocation = LocationData(
                                latitude = 6.4654,
                                longitude = 3.4064,
                                address = "Victoria Island"
                            ),
                            fare = 1500.0,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(rides.value) { ride ->
                RideItem(ride)
            }
        }
    }
}

@Composable
fun RideItem(ride: Ride) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Driver: ${ride.driver.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "From: ${ride.startLocation.address}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "To: ${ride.endLocation.address}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fare: ₦${ride.fare}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Timestamp: ${ride.timestamp}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}