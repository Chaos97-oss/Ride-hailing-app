package com.example.ridehaillingapp.di

import android.content.Context
import androidx.room.Room
import com.example.ridehaillingapp.data.local.RideDao
import com.example.ridehaillingapp.data.local.RideDatabase
import com.example.ridehaillingapp.data.repository.RideRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRideDatabase(@ApplicationContext context: Context): RideDatabase {
        return Room.databaseBuilder(
            context,
            RideDatabase::class.java,
            "ride_database"
        ).build()
    }

    @Provides
    fun provideRideDao(rideDatabase: RideDatabase): RideDao {
        return rideDatabase.rideDao()
    }

    @Provides
    fun provideRideRepository(rideDao: RideDao): RideRepository {
        return RideRepository(rideDao)
    }
}