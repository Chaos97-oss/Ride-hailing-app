package com.example.ridehaillingapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: DirectionsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionsApiService::class.java)
    }
}