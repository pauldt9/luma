package com.example.luma.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.luma.components.MainScaffold

@Composable
fun AiScreen(navController: NavController){
    MainScaffold(
        selectedItem = "ai",
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {

    }
}