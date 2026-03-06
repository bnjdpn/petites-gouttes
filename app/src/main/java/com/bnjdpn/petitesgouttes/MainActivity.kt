package com.bnjdpn.petitesgouttes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bnjdpn.petitesgouttes.ui.navigation.AppNavigation
import com.bnjdpn.petitesgouttes.ui.theme.PetitesGouttesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetitesGouttesTheme {
                AppNavigation()
            }
        }
    }
}
