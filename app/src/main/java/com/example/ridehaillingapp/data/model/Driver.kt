package com.example.ridehaillingapp.data.model
data class Driver(
    val name: String,
    val rating: Float = 5.0f,
    val licenseNumber: String = "UNKNOWN"
)