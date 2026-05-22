package com.example.luma.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.luma.components.MainScaffold

@Composable
fun HabitsScreen(navController: NavController){
    MainScaffold(
        selectedItem = "habits",
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {

    }
}