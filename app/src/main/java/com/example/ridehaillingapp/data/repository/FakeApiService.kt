package com.example.ridehaillingapp.data.repository

import com.example.ridehaillingapp.data.model.FareEstimate
import com.example.ridehaillingapp.data.model.RideConfirmation

import javax.inject.Inject

class FakeApiService @Inject constructor() {
    fun estimateFare(distanceKm: Double, timeMinutes: Double): FareEstimate {
        val estimatedFare = 5.0 + distanceKm * 2 + timeMinutes * 0.5
        return FareEstimate(distanceKm, timeMinutes, estimatedFare)
    }

    fun requestRide(): RideConfirmation {
        return RideConfirmation(
            status = "confirmed",
            driverName = "John Doe",
            car = "Toyota Prius",
            plateNumber = "XYZ-1234",
            eta = "5 min"
        )
    }
}