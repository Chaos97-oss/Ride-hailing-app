package com.example.ridehaillingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ridehaillingapp.data.model.Ride

@Database(
    entities = [Ride::class],
    version = 1,
    exportSchema = false
)
abstract class RideDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao
}