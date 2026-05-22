package com.example.luma.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.luma.components.MainScaffold

@Composable
fun NotesScreen(navController: NavController){
    MainScaffold(
        selectedItem = "notes",
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {

    }
}