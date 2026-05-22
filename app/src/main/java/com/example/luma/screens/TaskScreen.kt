package com.example.luma.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.luma.components.MainScaffold

@Composable
fun TaskScreen(navController: NavController){
    MainScaffold(
        selectedItem = "task",
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {

    }
}