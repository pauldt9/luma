package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AddFloatingButton
import com.example.luma.components.CardText
import com.example.luma.components.CardTitle
import com.example.luma.components.ItemCard
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.example.luma.model.Note
import com.example.luma.model.NoteLog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun NotesScreen(navController: NavController) {
    // Instancias de Firebase
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    // Lista de notas
    val notes = remember { mutableStateListOf<Note>() }

    // Cargar las notas de Firestore cuando el usuario cambia
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            db.collection("notes")
                .whereEqualTo("userId", currentUser.uid) // Filtra por el usuario actual
                .addSnapshotListener { snapshot, e -> // Escucha los cambios en la base de datos
                    if (e != null) {
                        return@addSnapshotListener // Si hay un error, no hacemos nada
                    }

                    // Si no hay error, actualizamos la lista de notas
                    if (snapshot != null) {
                        // Limpiamos la lista de notas y agregamos las nuevas notas
                        notes.clear()
                        val items = snapshot.toObjects(Note::class.java)
                        notes.addAll(items.sortedByDescending { it.timestamp })
                    }
                }
        } else {
            notes.clear()
        }
    }

    MainScaffold(
        selectedItem = "notes",
        onBottomItemClick = { route ->
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true

                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        },
        floatingActionButton = {
            AddFloatingButton(
                onClick = {
                    navController.navigate("note_detail"){
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        NotesHeader(notes)

        Spacer(modifier = Modifier.height(20.dp))

        NotesList(
            notes = notes,
            onEditClick = { note ->
                navController.navigate("note_detail/${note.id}")
            },
            onDeleteClick = { note ->
                db.collection("notes")
                    .document(note.id)
                    .delete()
                    .addOnSuccessListener {
                        saveNoteLog(
                            userId = note.userId,
                            noteId = note.id,
                            title = note.title,
                            content = note.content,
                            action = "eliminada"
                        )
                    }
            }
        )
    }
}

@Composable
private fun NotesHeader(notes: List<Note>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(stringResource(id = R.string.notes_title))
        ScreenSubtitle("Tienes ${notes.size} nota/s guardadas")
    }
}

@Composable
private fun NotesList(
    notes: List<Note>,
    onEditClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notes) { note ->
            NoteContainer(
                note = note,
                onClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun NoteContainer(
    note: Note,
    onClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ItemCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        onClick = { onClick(note) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
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

        Text(
            text = note.date,
            color = colorResource(id = R.color.date_note_col),
            fontSize = 12.sp
        )

        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = colorResource(id = R.color.options_icon_col)
            )
        }

        Box {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = colorResource(id = R.color.container_bg)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.edit_option),
                            color = colorResource(id = R.color.text_color)
                        )
                    },
                    onClick = {
                        expanded = false
                        onClick(note)
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete_option),
                            color = Color.Red
                        )
                    },
                    onClick = {
                        expanded = false
                        onDeleteClick(note)
                    }
                )
            }
        }
    }
}

// Guarda el registro de la nota en Firestore
private fun saveNoteLog(
    userId: String,
    noteId: String,
    title: String,
    content: String,
    action: String
) {
    val db = Firebase.firestore
    val logRef = db.collection("notes_log").document()

    val log = NoteLog(
        id = logRef.id,
        userId = userId,
        noteId = noteId,
        title = title,
        content = content,
        action = action
    )

    logRef.set(log)
}