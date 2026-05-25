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
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppCard
import com.example.luma.components.CardText
import com.example.luma.components.CardTitle
import com.example.luma.components.MainScaffold
import com.example.luma.components.MetricValue
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    // Contenido de la pantalla principal
    MainScaffold(
        selectedItem = "home",
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {
        HomeHeader(
            userName,
            onProfileClick = { navController.navigate("profile_screen") }
        )

        Spacer(modifier = Modifier.height(24.dp))
        // Botones para navegar a diferentes pantallas
        HomeBody(
            onTaskClick = { navController.navigate("task") },
            onNotesClick = { navController.navigate("notes") },
            onHabitsClick = { navController.navigate("habits") },
            onAiClick = { navController.navigate("ai") }
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
    onTaskClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHabitsClick: () -> Unit,
    onAiClick: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(21.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            AppCard(
                modifier = Modifier.weight(1f).height(130.dp),
                onClick = onTaskClick
            ) {
                CardTitle(stringResource(id = R.string.task_title))
                Spacer(modifier = Modifier.height(3.dp))
                MetricValue("0")
            }

            AppCard(
                modifier = Modifier.weight(1f).height(130.dp),
                onClick = onNotesClick
            ) {
                CardTitle(stringResource(id = R.string.notes_title))
                Spacer(modifier = Modifier.height(3.dp))
                MetricValue("0")
            }
        }

        AppCard(
            modifier = Modifier.fillMaxWidth().height(130.dp),
            onClick = onHabitsClick
        ) {
            CardTitle(stringResource(id = R.string.habits_title))
            Spacer(modifier = Modifier.height(14.dp))
            CardText(stringResource(id = R.string.habits_subtitle))
        }

        AppCard(
            modifier = Modifier.fillMaxWidth().height(130.dp),
            onClick = onAiClick
        ) {
            CardTitle(stringResource(id = R.string.ai_title))
            Spacer(modifier = Modifier.height(12.dp))
            CardText(stringResource(id = R.string.ai_subtitle))
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
