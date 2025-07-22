package com.example.ridehaillingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ridehaillingapp.ui.history.HistoryScreen
import com.example.ridehaillingapp.ui.main.MainScreen
import com.example.ridehaillingapp.viewmodel.RideViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = "main"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainScreen(navController = navController)
        }
        composable("history") {
            HistoryScreen(navController = navController)
        }
    }
}