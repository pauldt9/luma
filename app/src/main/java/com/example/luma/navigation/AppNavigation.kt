package com.example.luma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luma.screens.AiScreen
import com.example.luma.screens.HabitsScreen
import com.example.luma.screens.HomeScreen
import com.example.luma.screens.LoginScreen
import com.example.luma.screens.NotesScreen
import com.example.luma.screens.SignUpScreen
import com.example.luma.screens.TaskScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController)
        }
        composable("sign-up"){
            SignUpScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("task"){
            TaskScreen(navController)
        }
        composable("notes"){
            NotesScreen(navController)
        }
        composable("habits"){
            HabitsScreen(navController)
        }
        composable("ai"){
            AiScreen(navController)
        }
    }
}