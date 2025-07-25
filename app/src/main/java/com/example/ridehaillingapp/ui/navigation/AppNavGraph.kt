package com.example.ridehaillingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ridehaillingapp.ui.history.HistoryScreen
import com.example.ridehaillingapp.ui.main.MainScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLocationPermissionGranted: Boolean,
    startDestination: String = "main"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainScreen(
                navController = navController,
                isLocationPermissionGranted = isLocationPermissionGranted
            )
        }
        composable("history") {
            HistoryScreen(navController = navController)
        }
    }
}