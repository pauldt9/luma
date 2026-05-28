package com.example.luma.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.luma.components.MainScaffold

@Composable
fun NotesScreen(navController: NavController){
    MainScaffold(
        selectedItem = "notes",
        onBottomItemClick = { route ->
            // Navega a la pantalla seleccionada
            navController.navigate(route) {
                // Mantener el estado de la pantalla
                launchSingleTop = true
                restoreState = true

                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    ) {

    }
}