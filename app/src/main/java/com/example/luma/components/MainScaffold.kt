package com.example.luma.components

import androidx.compose.foundation.layout.Box
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
 * Layout principal para las pantallas:
 * HomeScreen, TaskScreen, NotesScreen, HabitsScreen y AiScreen.
 */

@Composable
fun MainScaffold(
    selectedItem: String, // Indica que icono marcar en la barra de navegacion
    onBottomItemClick: (String) -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null, // Para un boton flotante (opcional)
    content: @Composable ColumnScope.() -> Unit
){
    AppBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                // Barra de navegacion

                Box(
                    modifier = Modifier.padding(
                        start = 17.dp,
                        end = 17.dp,
                        bottom = 25.dp
                    )
                ) {
                    AppBottomBar(
                        selectedItem = selectedItem,
                        onItemClick = onBottomItemClick
                    )
                }
            },
            // Boton flotante (+)
            floatingActionButton = {
                floatingActionButton?.invoke()
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 30.dp),
                content = content
            )
        }
    }
}