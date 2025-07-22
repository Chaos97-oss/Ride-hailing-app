package com.example.ridehaillingapp.util

import com.example.ridehaillingapp.data.repository.FakeApiService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RideConfirmationTest {

    private lateinit var fakeApiService: FakeApiService

    @Before
    fun setup() {
        fakeApiService = FakeApiService()
    }

    @Test
    fun testRideConfirmation() {
        val result = fakeApiService.requestRide()
        Assert.assertEquals("confirmed", result.status)
        Assert.assertEquals("John Doe", result.driverName)
        Assert.assertEquals("Toyota Prius", result.car)
        Assert.assertEquals("XYZ-1234", result.plateNumber)
        Assert.assertEquals("5 min", result.eta)
    }
}