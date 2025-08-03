package com.example.ridehaillingapp.util

import android.util.Log
import com.example.ridehaillingapp.BuildConfig
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

suspend fun decodePolyline(userLatLng: LatLng, destinationLatLng: LatLng): List<LatLng> {
    return withContext(Dispatchers.IO) {
        try {
            val origin = "${userLatLng.latitude},${userLatLng.longitude}"
            val destination = "${destinationLatLng.latitude},${destinationLatLng.longitude}"
            val apiKey = BuildConfig.MAPS_API_KEY
            val urlString = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=${URLEncoder.encode(origin, "UTF-8")}" +
                    "&destination=${URLEncoder.encode(destination, "UTF-8")}" +
                    "&key=$apiKey"

            val response = URL(urlString).readText()
            val jsonObject = JSONObject(response)

            val routes = jsonObject.getJSONArray("routes")
            if (routes.length() == 0) return@withContext emptyList()

            val overviewPolyline = routes.getJSONObject(0)
                .getJSONObject("overview_polyline")
                .getString("points")

            decodePolylineString(overviewPolyline)
        } catch (e: Exception) {
            Log.e("PolylineError", "Failed to decode polyline: ${e.message}")
            emptyList()
        }
    }
}

private fun decodePolylineString(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val point = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
        poly.add(point)
    }

    return poly
}