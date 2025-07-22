package com.example.ridehaillingapp.data.repository



import com.example.ridehaillingapp.data.local.RideDao
import com.example.ridehaillingapp.data.model.Ride
import javax.inject.Inject

class RideRepository @Inject constructor(private val dao: RideDao) {

    suspend fun insertRide(ride: Ride) = dao.insertRide(ride)

    fun getAllRides() = dao.getAllRides()

    suspend fun deleteRide(ride: Ride) = dao.deleteRide(ride)
}