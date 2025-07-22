package com.example.ridehaillingapp.util
import kotlinx.coroutines.flow.first
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ridehaillingapp.data.local.RideDao
import com.example.ridehaillingapp.data.local.RideDatabase
import com.example.ridehaillingapp.data.model.Driver
import com.example.ridehaillingapp.data.model.LocationData
import com.example.ridehaillingapp.data.model.Ride
import kotlinx.coroutines.runBlocking
import org.junit.*

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RideDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RideDatabase
    private lateinit var rideDao: RideDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RideDatabase::class.java
        ).allowMainThreadQueries().build()

        rideDao = database.rideDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveRide() = runBlocking {
        val ride = Ride(
            driver = Driver("John Doe", 4.8f, "XYZ-1234"),
            startLocation = LocationData(10.0, 20.0, "Start Point"),
            endLocation = LocationData(30.0, 40.0, "End Point"),
            fare = 15.0,
            timestamp = System.currentTimeMillis()
        )

        rideDao.insertRide(ride)

        val rides = rideDao.getAllRides().first()

        Assert.assertTrue(rides.isNotEmpty())
        Assert.assertEquals("John Doe", rides[0].driver.name)
        Assert.assertEquals("XYZ-1234", rides[0].driver.licenseNumber)
        Assert.assertEquals(15.0, rides[0].fare, 0.01)
    }
}
