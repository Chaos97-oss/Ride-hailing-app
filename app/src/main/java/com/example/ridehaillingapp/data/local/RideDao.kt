package com.example.ridehaillingapp.data.local

import androidx.room.*
import com.example.ridehaillingapp.data.model.Ride
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: Ride)

    @Delete
    suspend fun deleteRide(ride: Ride)

    @Query("SELECT * FROM ride_table ORDER BY timestamp DESC")
    fun getAllRides(): Flow<List<Ride>>
}