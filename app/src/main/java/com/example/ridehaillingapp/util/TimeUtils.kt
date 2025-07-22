package com.example.ridehaillingapp.util

object TimeUtils {
    fun formatTimestamp(timestamp: Long): String {
        return java.text.SimpleDateFormat("HH:mm, dd MMM yyyy").format(java.util.Date(timestamp))
    }
}