package com.example.ridehaillingapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.viewmodel.RideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: RideViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    RideHistoryScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RideHistoryScreen(viewModel: RideViewModel) {
    val rides by viewModel.rides.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(rides) { ride ->
            RideHistoryItem(ride)
        }
    }
}

@Composable
fun RideHistoryItem(ride: Ride) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Driver: ${ride.driver.name}")
            Text(text = "From: ${ride.startLocation.address}")
            Text(text = "To: ${ride.endLocation.address}")
            Text(text = "Fare: $${ride.fare}")
        }
    }
}