package com.example.luma.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.luma.navigation.AppBottomBar

/**
 * Layout principal para las pantallas HomeScreen, TaskScreen, NotesScreen, HabitsScreen y AiScreen.
 */

@Composable
fun MainScaffold(
//    selectedItem: String, // Indica que icono marcar en la barra de navegacion
//    floatingActionButton: @Composable (() -> Unit)? = null, // Para un boton flotante (opcional)
    content: @Composable ColumnScope.() -> Unit
){
    AppBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                // Barra de navegacion
//                AppBottomBar(
////                    navController = navController,
////                    selectedItem = selectedItem
//                )
            },
            // Boton flotante (+)
//            floatingActionButton = {
//                floatingActionButton?.invoke()
//            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 30.dp, vertical = 45.dp),
                content = content
            )
        }
    }
}