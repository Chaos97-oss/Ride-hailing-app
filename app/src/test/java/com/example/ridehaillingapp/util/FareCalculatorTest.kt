package com.example.ridehaillingapp.util

import org.junit.Test
import org.junit.Assert.assertEquals

class FareCalculatorTest {

    @Test
    fun testBasicFareCalculation() {
        val fare = FareCalculator.calculateFare(5.0, false, 1.0)
        assertEquals(7.50, fare, 0.01)
    }

    @Test
    fun testSurgePricing() {
        val fare = FareCalculator.calculateFare(8.0, true, 1.0)
        assertEquals(15.75, fare, 0.01)
    }

    @Test
    fun testTrafficSurge() {
        val fare = FareCalculator.calculateFare(6.0, false, 1.3)
        assertEquals(11.05, fare, 0.01)
    }
}