package com.example.ridehaillingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import com.example.ridehaillingapp.model.LocationData

@Entity(tableName = "ride_table")
data class Ride(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Embedded(prefix = "driver_")
    val driver: Driver,

    @Embedded(prefix = "start_")
    val startLocation: LocationData,

    @Embedded(prefix = "end_")
    val endLocation: LocationData,

    val fare: Double,

    val timestamp: Long
)