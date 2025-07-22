package com.example.ridehaillingapp.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ridehaillingapp.data.model.Ride

@Composable
fun HistoryItem(ride: Ride) {
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