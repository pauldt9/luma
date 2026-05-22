package com.example.luma.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.AppCard
import com.example.luma.components.CardSubtitle
import com.example.luma.components.CardTitle
import com.example.luma.components.MainScaffold
import com.example.luma.components.MetricValue
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle

@Composable
fun HomeScreen(navController: NavController){
    MainScaffold(
        selectedItem = "home",
        // Determina donde el usuario hizo clic y el navController lo dirige a la pantalla correcta
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {
        HomeHeader()

        Spacer(modifier = Modifier.height(24.dp))

        HomeBody()
    }
}

@Composable
private fun HomeHeader(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bienvenida y fecha
        Column {
            ScreenTitle(stringResource(id = R.string.home_greeting))
            ScreenSubtitle(stringResource(id = R.string.home_date))
        }

        UserIcon()
    }
}

@Composable
private fun HomeBody(){
    Column(
        verticalArrangement = Arrangement.spacedBy(21.dp)
    ) {
        // Contenedor Tareas y Notas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            // Tareas
            AppCard(
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
            ) {
                CardTitle(stringResource(id = R.string.task_title))

                Spacer(modifier = Modifier.height(3.dp))

                MetricValue("0") // Cantidad de TAREAS en total
            }

            // Notas
            AppCard(
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
            ) {
                CardTitle(stringResource(id = R.string.notes_title))

                Spacer(modifier = Modifier.height(3.dp))

                MetricValue("0") // Cantidad de NOTAS en total
            }
        }

        // Habitos
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            CardTitle(stringResource(id = R.string.habits_title))

            Spacer(modifier = Modifier.height(14.dp))

            CardSubtitle(stringResource(id = R.string.habits_subtitle))
        }

        // IA
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
        ) {
            CardTitle(stringResource(id = R.string.ai_title))

            Spacer(modifier = Modifier.height(12.dp))

            CardSubtitle(stringResource(id = R.string.ai_subtitle))
        }
    }
}

// Icono de usuario
@Composable
private fun UserIcon() {
    Box(
        modifier = Modifier
            .size(45.dp)
            .background(
                color = colorResource(id = R.color.btn_action_background),
                shape = CircleShape
            ),
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