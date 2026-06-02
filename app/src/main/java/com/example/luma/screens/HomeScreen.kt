@file:Suppress("DEPRECATION")

package com.example.luma.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppCard
import com.example.luma.components.CardText
import com.example.luma.components.CardTitle
import com.example.luma.components.MainScaffold
import com.example.luma.components.MetricValue
import com.example.luma.components.ProgressBar
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController){
    val auth = Firebase.auth // Instancia de autenticación de Firebase
    val db = Firebase.firestore // Instancia de Firestore de Firebase
    val currentUser = auth.currentUser // Obtiene el usuario actual

    // El estado empieza en null para indicar "cargando"
    var userName by remember { mutableStateOf<String?>(null) }

    // Estados de las metricas
    val totalTasks = 5
    val pendingTasks = 3
    val completedTasks = 2
    val taskPercentage = 40

    val totalNotes = 3
    val lastNoteUpdate = 2

    val currentStreak = 3
    val completedHabits = 2
    val totalHabits = 3
    val habitProgress = completedHabits.toFloat() / totalHabits
    val lastAiQuery = "hoy"

    // Obtener el nombre del usuario desde Firestore
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    userName = document.getString("name") ?: "Usuario"
                }
                .addOnFailureListener { userName = "Usuario" }
        } ?: run { userName = "Invitado" }
    }

    fun navigateToMainRoute(route: String) {
        // Navega a la pantalla seleccionada
        navController.navigate(route) {
            // Mantener el estado de la pantalla
            launchSingleTop = true
            restoreState = true

            popUpTo("home") {
                saveState = true
            }
        }
    }

    // Contenido de la pantalla principal
    MainScaffold(
        selectedItem = "home",
        onBottomItemClick = { route ->
            navigateToMainRoute(route)
        }
    ) {
        HomeHeader(
            userName,
            onProfileClick = { navController.navigate("profile_screen") }
        )

        Spacer(modifier = Modifier.height(24.dp))
        // Botones para navegar a diferentes pantallas
        HomeBody(
            totalTasks = totalTasks,
            pendingTasks = pendingTasks,
            completedTasks = completedTasks,
            taskPercentage = taskPercentage,

            totalNotes = totalNotes,
            lastNoteUpdate = lastNoteUpdate,

            currentStreak = currentStreak,
            habitProgress = habitProgress, // Progreso de la barra de habitos
            completedHabits = completedHabits,
            totalHabits = totalHabits,

            lastAiQuery = lastAiQuery,

            onTaskClick = { navigateToMainRoute("task") },
            onNotesClick = { navigateToMainRoute("notes") },
            onHabitsClick = { navigateToMainRoute("habits") },
            onAiClick = { navigateToMainRoute("ai") }
        )
    }

}

@OptIn(ExperimentalAnimationApi::class) // Necesario para AnimatedContent
@Composable
private fun HomeHeader(userName: String?, onProfileClick: () -> Unit){
    // Fecha actual formateada
    val currentDate = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES")).format(Date())
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            // Utilizamos AnimatedContent para que el cambio de texto sea suave
            AnimatedContent(
                targetState = userName,
                transitionSpec = {
                    fadeIn().togetherWith(fadeOut())
                }
            ) { targetName ->
                if (targetName == null) {
                    // Animación de carga para el saludo completo
                    ShimmerGreeting()
                } else {
                    ScreenTitle("Hola, $targetName")
                }
            }
            ScreenSubtitle(currentDate.replaceFirstChar { it.uppercase() })
        }

        UserIcon(onClick = onProfileClick) // Icono de perfil con acción
    }
}

// Animacion de carga
@Composable
private fun ShimmerGreeting() {
    val shimmerColors = listOf(
        colorResource(id = R.color.textfield_bg_col).copy(alpha = 0.6f),
        colorResource(id = R.color.textfield_bg_col).copy(alpha = 0.2f),
        colorResource(id = R.color.textfield_bg_col).copy(alpha = 0.6f),
    )

    // Transición de la animación
    val transition = rememberInfiniteTransition()
    // Animación de desplazamiento
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Efecto de la animación
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(brush)
    )
}

// Cuerpo de la pantalla principal
@Composable
private fun HomeBody(
    habitProgress: Float,
    totalTasks: Int,
    pendingTasks: Int,
    completedTasks: Int,
    taskPercentage: Int,
    totalNotes: Int,
    lastNoteUpdate: Int,
    currentStreak: Int,
    completedHabits: Int,
    totalHabits: Int,
    lastAiQuery: String,
    onTaskClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHabitsClick: () -> Unit,
    onAiClick: () -> Unit
){
    // TODO: Mostrar la cantidad de tareas, notas y habitos creados.
    Column(
        verticalArrangement = Arrangement.spacedBy(21.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tareas
            AppCard(
                modifier = Modifier.weight(1f).height(155.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                onClick = onTaskClick
            ) {
                CardTitle(stringResource(id = R.string.task_title))
                MetricValue(totalTasks.toString())
                Spacer(modifier = Modifier.height(10.dp))
                // Tareas pendendites
                CardText(
                    text = stringResource(id = R.string.pending_tasks_home_screen, pendingTasks),
                    fontSize = 15.sp
                )
                // Tareas completadas y porcentake
                CardText(
                    text = stringResource(id = R.string.completed_tasks_home_screen, completedTasks, taskPercentage),
                    fontSize = 15.sp
                )
            }

            // Notas
            AppCard(
                modifier = Modifier.weight(1f).height(155.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                onClick = onNotesClick
            ) {
                CardTitle(stringResource(id = R.string.notes_title))
                MetricValue(totalNotes.toString())
                Spacer(modifier = Modifier.height(31.dp))
                // Ultima edicion de notas (ultima creada o editada)
                CardText(stringResource(id = R.string.last_update_notes, lastNoteUpdate))
            }
        }

        // Habitos
        AppCard(
            modifier = Modifier.fillMaxWidth().height(145.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            onClick = onHabitsClick
        ) {
            CardTitle(stringResource(id = R.string.habits_title))
            // Racha del usuario
            CardText(stringResource(id = R.string.habits_streak, currentStreak))
            Spacer(modifier = Modifier.height(15.dp))
            CardText(stringResource(id = R.string.habits_subtitle))
            // Barra de progreso
            ProgressBar(habitProgress)
            // Habitos completados
            CardText(stringResource(id = R.string.habits_completed, completedHabits, totalHabits))
        }

        // IA
        AppCard(
            modifier = Modifier.fillMaxWidth().height(140.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            onClick = onAiClick
        ) {
            CardTitle(stringResource(id = R.string.ai_title))
            CardText(stringResource(id = R.string.ai_subtitle))
            Spacer(modifier = Modifier.height(19.dp))
            CardText(stringResource(id = R.string.ai_last_query, lastAiQuery))
        }
    }
}

// Icono de perfil con acción
@Composable
private fun UserIcon(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .background(
                color = colorResource(id = R.color.btn_action_background),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = colorResource(id = R.color.btn_action_text),
            modifier = Modifier.size(30.dp)
        )
    }
}
