package com.example.luma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luma.screens.AddHabitScreen
import com.example.luma.screens.AddTaskScreen
import com.example.luma.screens.AiScreen
import com.example.luma.screens.EditHabitScreen
import com.example.luma.screens.EditTaskScreen
import com.example.luma.screens.HabitsScreen
import com.example.luma.screens.HomeScreen
import com.example.luma.screens.LoginScreen
import com.example.luma.screens.NoteDetailScreen
import com.example.luma.screens.NotesScreen
import com.example.luma.screens.ProfileScreen
import com.example.luma.screens.SignUpScreen
import com.example.luma.screens.TaskScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AppNavigation(){
    // Controlador de navegación
    val navController = rememberNavController()
    
    // Forzar el cierre de sesión para limpiar el estado persistente(temporal)
    Firebase.auth.signOut()

    // Obtiene al usuario autenticado actualmente (sujeto a cambios)
    val currentUser = Firebase.auth.currentUser
    // Pantalla inicial
    // "Home" si hay sesión iniciada, de lo contrario "Login"
    val startDestination = if (currentUser != null) "home" else "login"

    NavHost(navController, startDestination = startDestination){
        // Pantalla de inicio de sesión
        composable("login"){
            LoginScreen(navController)
        }
        // Pantalla de registro de usuario
        composable("sign_up"){
            SignUpScreen(navController)
        }
        // Pantalla principal
        composable("home") {
            HomeScreen(navController)
        }
        composable("task"){
            TaskScreen(navController)
        }
        composable ("add_task") {
            AddTaskScreen(navController)
        }
        composable ("edit_task"){
            EditTaskScreen(navController)
        }
        composable("notes"){
            NotesScreen(navController)
        }
        composable ("note_detail"){
            NoteDetailScreen(navController)
        }
        composable("habits"){
            HabitsScreen(navController)
        }
        composable ("add_habit"){
            AddHabitScreen(navController)
        }
        composable ("edit_habit"){
            EditHabitScreen(navController)
        }
        composable("ai"){
            AiScreen(navController)
        }
        composable ("profile_screen") {
            ProfileScreen(navController)
        }
    }
}