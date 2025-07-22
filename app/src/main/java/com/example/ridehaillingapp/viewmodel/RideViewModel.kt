package com.example.ridehaillingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridehaillingapp.data.model.Ride
import com.example.ridehaillingapp.data.repository.RideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val repository: RideRepository
) : ViewModel() {

    val rides: StateFlow<List<Ride>> = repository.getAllRides()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
}