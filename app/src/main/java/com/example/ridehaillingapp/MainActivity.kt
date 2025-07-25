package com.example.ridehaillingapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.ridehaillingapp.ui.navigation.AppNavGraph
import com.example.ridehaillingapp.ui.theme.RideHailingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RideHailingAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    var locationPermissionGranted by remember { mutableStateOf(false) }


                    val permissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                    locationPermissionGranted = isGranted

                    }


                    LaunchedEffect(Unit) {
                        val isGranted = ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                        if (!isGranted) {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        } else {
                            locationPermissionGranted = true
                        }
                    }


                    AppNavGraph(
                        navController = navController,
                        isLocationPermissionGranted = locationPermissionGranted
                    )
                }
            }
        }
    }
}