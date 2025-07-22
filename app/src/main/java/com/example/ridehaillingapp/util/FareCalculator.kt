package com.example.ridehaillingapp.util

import com.example.ridehaillingapp.data.model.FareEstimate


object FareCalculator {
    fun calculateFare(distanceKm: Double, isPeakHour: Boolean, trafficLevel: Double): Double {
        val baseFare = 2.50
        val perKmRate = 1.00
        val peakMultiplier = if (isPeakHour) 1.5 else 1.0
        return (baseFare + (distanceKm * perKmRate)) * peakMultiplier * trafficLevel
    }
}