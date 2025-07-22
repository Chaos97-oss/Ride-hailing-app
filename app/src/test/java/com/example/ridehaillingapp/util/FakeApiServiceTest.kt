package com.example.ridehaillingapp.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class FakeApiServiceTest {

    private val fakeApiService = FakeApiService()

    @Test
    fun testEstimateFare() {
        val result = fakeApiService.estimateFare(5.0, 10.0)
        val expectedFare = 5.0 + 5.0 * 2 + 10.0 * 0.5
        assertEquals(expectedFare, result.estimatedFare, 0.01)
    }

    @Test
    fun testRequestRide() {
        val result = fakeApiService.requestRide()
        assertEquals("confirmed", result.status)
        assertEquals("John Doe", result.driverName)
    }
}