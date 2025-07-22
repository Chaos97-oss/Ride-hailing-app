package com.example.ridehaillingapp.data.model

data class RideConfirmation(
    val status: String,
    val driverName: String,
    val car: String,
    val plateNumber: String,
    val eta: String
)