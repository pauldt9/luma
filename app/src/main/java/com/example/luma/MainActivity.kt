package com.example.luma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.luma.navigation.AppNavigation
import com.example.luma.ui.theme.LumaTheme
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar Firebase
        Firebase.initialize(this)
        
        // Inicializar App Check con Play Integrity
        val firebaseAppCheck = Firebase.appCheck
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        enableEdgeToEdge()

        setContent {
            LumaTheme {
                AppNavigation()
            }
        }
    }
}
