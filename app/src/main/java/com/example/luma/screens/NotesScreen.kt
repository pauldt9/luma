package com.example.luma.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AddFloatingButton
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.example.luma.model.Note
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.luma.components.CardText
import com.example.luma.components.CardTitle
import com.example.luma.components.ItemCard

@Composable
fun NotesScreen(navController: NavController){
    // Lista de notas
//    val notes = remember { mutableStateListOf<Note>() }

    // Solo para pruebas, eliminarlo
    val notes = remember {
        mutableStateListOf(
            Note(
                id = 1,
                title = "Ideas para el proyecto",
                content = "Agregar pantalla de notas, mejorar navegación y revisar estilos antes de conectar Firebase.",
                date = "Mayo 5"
            ),
            Note(
                id = 2,
                title = "Pendientes de programación móvil",
                content = "Terminar los componentes reutilizables, revisar la pantalla de tareas y preparar la rama para el equipo.",
                date = "Mayo 7"
            ),
            Note(
                id = 3,
                title = "Recordatorio",
                content = "No olvidar hacer commit antes de cambiar de rama y revisar git status antes de hacer merge.",
                date = "Mayo 9"
            ),
            Note(
                id = 4,
                title = "Nota con título muy largo para probar que el texto se corte correctamente",
                content = "Este contenido también es bastante largo para verificar que el overflow con ellipsis funcione bien dentro del contenedor.",
                date = "Mayo 12"
            )
        )
    }


    MainScaffold(
        selectedItem = "notes",
        onBottomItemClick = { route ->
            navController.navigate(route)
        },
        floatingActionButton = {
            AddFloatingButton(
                onClick = {
                    navController.navigate("note_detail")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Header
            ScreenTitle(stringResource(id = R.string.notes_title))
            ScreenSubtitle(stringResource(id = R.string.notes_count))

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de notas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(notes) { note ->
                    NoteContainer(
                        note = note,
                        onClick = {
                            navController.navigate("note_detail")
                        },
                        onDeleteClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteContainer(
    note: Note,
    onClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    ItemCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        onClick = { onClick(note) }, // El contenedor se hace boton
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ){

        // Titulo y contenido
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CardTitle(
                text = note.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            CardText(
                text = note.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        // Fecha de creacion
        Text(
            text = note.date,
            color = colorResource(id = R.color.date_note_col),
            fontSize = 12.sp
        )

        // Icono menu desplegable
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = colorResource(id = R.color.options_icon_col)
            )
        }

        // Boton eliminar
        Box {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = colorResource(id = R.color.container_bg)
            ) {
                // Eliminar
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.delete_option), color = Color.Red) },
                    onClick = {
                        expanded = false
                        onDeleteClick(note)
                    }
                )
            }
        }
    }
}