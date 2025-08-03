package com.example.ridehaillingapp.data.api
import com.example.ridehaillingapp.data.model.DirectionModels
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface DirectionsApiService {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionModels.DirectionsResponse
}