package com.example.ridehaillingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.data.model.RideConfirmation
import com.example.ridehaillingapp.data.repository.FakeApiService
import com.example.ridehaillingapp.data.repository.RideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val repository: RideRepository,
    private val apiService: FakeApiService
) : ViewModel() {


    val rides: StateFlow<List<Ride>> = repository.getAllRides()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    private val _fareEstimate = MutableStateFlow<Double?>(null)
    val fareEstimate: StateFlow<Double?> = _fareEstimate


    private val _rideConfirmation = MutableStateFlow<RideConfirmation?>(null)
    val rideConfirmation: StateFlow<RideConfirmation?> = _rideConfirmation

    fun addRide(ride: Ride) {
        viewModelScope.launch {
            repository.insertRide(ride)
        }
    }

    fun deleteRide(ride: Ride) {
        viewModelScope.launch {
            repository.deleteRide(ride)
        }
    }


    fun calculateFare(distanceKm: Double, timeMinutes: Double) {
        val estimate = apiService.estimateFare(distanceKm, timeMinutes)
        _fareEstimate.value = estimate.estimatedFare
    }


    fun requestRide() {
        val confirmation = apiService.requestRide()
        _rideConfirmation.value = confirmation
    }
}