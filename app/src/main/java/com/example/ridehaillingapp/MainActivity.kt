package com.example.ridehaillingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.ridehaillingapp.ui.ridelist.RideListScreen
import com.example.ridehaillingapp.ui.theme.RideHaillingAppTheme
//import androidx.activity.compose.setContent
import com.example.ridehaillingapp.ui.main.MainScreen
//import com.example.ridehaillingapp.ui.theme.RideHaillingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RideHaillingAppTheme {
                MainScreen()
            }
        }
    }
}