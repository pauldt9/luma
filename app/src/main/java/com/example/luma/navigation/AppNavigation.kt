package com.example.luma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

@Composable
fun AppNavigation(){
    // Controlador de navegación
    val navController = rememberNavController()
    
    // LÍNEA PARA FORZAR CIERRE DE SESIÓN (Eliminar después de integrar manejo de perfil)
    // Firebase.auth.signOut()

    // Obtiene al usuario autenticado actualmente
    val currentUser = Firebase.auth.currentUser

    // Pantalla inicial: "Home" si hay sesión iniciada, de lo contrario "Login"
    val startDestination = if (currentUser != null) "home" else "login"

    NavHost(navController, startDestination = startDestination){
        composable("login"){
            LoginScreen(navController)
        }
        composable("sign_up"){
            SignUpScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("task"){
            TaskScreen(navController)
        }
        composable ("add_task") {
            AddTaskScreen(navController)
        }
        // Ruta para editar tarea pasando el ID como parámetro
        composable (
            "edit_task/{taskId}",
            // Se declara la lista de argumentos que recibirá esta ruta.
            arguments = listOf(
                // Se declara un argumento de tipo String llamado "taskId"
                navArgument("taskId") {type = NavType.StringType }
            )
        ){ backStackEntry ->
            // Se obtiene el valor del argumento "taskId" de la entrada de la pila de navegación
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            EditTaskScreen(navController, taskId) // Se llama a la pantalla de edición de tarea, con el ID correspondiente a la tarea
        }
        composable("notes"){
            NotesScreen(navController)
        }
        composable ("note_detail"){
            NoteDetailScreen(navController)
        }

        // Ruta para editar tarea pasando el ID como parámetro
        composable(
            "note_detail/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteDetailScreen(navController, noteId)
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