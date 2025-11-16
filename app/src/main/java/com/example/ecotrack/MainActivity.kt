package com.example.ecotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ecotrack.AppNavHost
import com.example.ecotrack.ui.theme.EcoTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoTrackTheme {
                AppNavHost()
            }
        }
    }
}