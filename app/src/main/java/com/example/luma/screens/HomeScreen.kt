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
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle

@Composable
fun HomeScreen(navController: NavController){
    MainScaffold {
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
    Column() {
        // Card de Tareas y Notas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            // Tareas
            AppCard(
                modifier = Modifier
                    .width(160.dp)
                    .height(130.dp)
            ) {

            }

            // Notas
            AppCard(
                modifier = Modifier
                    .width(160.dp)
                    .height(130.dp)
            ) {

            }
        }


        AppCard() {

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
            contentDescription = "Perfil",
            tint = colorResource(id = R.color.btn_action_text),
            modifier = Modifier.size(30.dp)
        )
    }
}