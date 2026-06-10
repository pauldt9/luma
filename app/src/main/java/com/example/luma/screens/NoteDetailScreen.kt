package com.example.luma.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.BackButton
import com.example.luma.model.Note
import com.example.luma.model.NoteLog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

private val noteGroups = listOf(
    "General",
    "Escuela",
    "Trabajo",
    "Personal",
    "Ideas"
)
@Composable
fun NoteDetailScreen(
    navController: NavController,
    noteId: String? = null
) {
    // Instancias de Firebase
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    // Estado para los campos de texto
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    // Estado para el nombre del grupo
    var groupName by remember { mutableStateOf("General") }


    // Cargar los datos de la nota si se proporciona un ID
    LaunchedEffect(noteId) {
        if (!noteId.isNullOrEmpty()) {
            db.collection("notes")
                .document(noteId)
                .get()
                .addOnSuccessListener { document ->
                    // Si el documento existe, cargar los datos en los campos de texto
                    val note = document.toObject(Note::class.java)

                    if (note != null) {
                        title = note.title
                        content = note.content
                        groupName = note.groupName.ifBlank { "General" }
                    }
                }
        }
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp),
            horizontalAlignment = Alignment.Start
        ) {
            BackButton(
                onClick = {
                    saveNote(
                        noteId = noteId,
                        title = title,
                        content = content,
                        groupName = groupName,
                        userId = currentUser?.uid ?: "",
                        onSuccess = {
                            navController.popBackStack()
                        },
                        onError = { error ->
                            println("Error al guardar la nota: ${error.message}")

                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            ) {
                NoteTextField(
                    value = title,
                    placeholder = stringResource(id = R.string.title_placeholder),
                    onValueChange = { title = it }
                )

                Spacer(modifier = Modifier.height(15.dp))

                NoteGroupSelector(
                    selectedGroup = groupName,
                    onGroupSelected = { groupName = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                NoteTextField(
                    value = content,
                    placeholder = stringResource(id = R.string.content_placeholder),
                    fontSize = 16.sp,
                    onValueChange = { content = it },
                    singleLine = false
                )
            }
        }
    }
}

private fun saveNote(
    noteId: String?,
    title: String,
    content: String,
    groupName: String,
    userId: String,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    if (title.isBlank() && content.isBlank()){
        onSuccess()
        return
    }

    if (userId.isBlank()){
        onError(Exception("Usuario no autenticado"))
        return
    }

    // Instancias de Firebase
    val db = Firebase.firestore

    // Formatear la fecha
    val date = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())

    if (noteId.isNullOrEmpty()) {
        val noteRef = db.collection("notes").document()

        val finalGroupName = groupName.ifBlank { "General" }

        // Crear un objeto Note con los datos
        val note = Note(
            id = noteRef.id,
            userId = userId,
            groupName = finalGroupName,
            title = title,
            content = content,
            date = date,
            timestamp = System.currentTimeMillis()
        )

        // Guardar la nota en Firestore
        noteRef.set(note)
            .addOnSuccessListener {
                saveNoteLog(
                    userId = userId,
                    noteId = noteRef.id,
                    newTitle = title,
                    newContent = content,
                    newGroupName = finalGroupName,
                    action = "creada"
                )
                onSuccess() // Si se guarda correctamente, llamamos a onSuccess
            }
            .addOnFailureListener { error ->
                onError(error) // Si hay un error, llamamos a onError
            }
    } else {
        val noteRef = db.collection("notes").document(noteId)
        val finalGroupName = groupName.ifBlank { "General" }

        noteRef.get()
            .addOnSuccessListener { document ->
                val oldNote = document.toObject(Note::class.java)

                noteRef.update(
                    mapOf(
                        "title" to title,
                        "content" to content,
                        "groupName" to finalGroupName,
                        "date" to date,
                        "timestamp" to System.currentTimeMillis()
                    )
                )
                    .addOnSuccessListener {
                        saveNoteLog(
                            userId = userId,
                            noteId = noteId,
                            oldTitle = oldNote?.title ?: "",
                            oldContent = oldNote?.content ?: "",
                            oldGroupName = oldNote?.groupName ?: "General",
                            newTitle = title,
                            newContent = content,
                            newGroupName = finalGroupName,
                            action = "actualizada"
                        )

                        onSuccess()
                    }
                    .addOnFailureListener { error ->
                        onError(error)
                    }
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }
}

@Composable
private fun NoteTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    fontSize: TextUnit = 32.sp,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    Box(
        modifier = modifier
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontSize = fontSize,
                color = colorResource(id = R.color.placeholder_col)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = fontSize,
                color = colorResource(id = R.color.text_color)
            ),
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Guarda el registro de la nota en Firestore
private fun saveNoteLog(
    userId: String,
    noteId: String,
    oldTitle: String = "",
    oldContent: String = "",
    oldGroupName: String = "",
    newTitle: String = "",
    newContent: String = "",
    newGroupName: String = "",
    action: String
) {
    val db = Firebase.firestore
    val logRef = db.collection("notes_log").document()

    val log = NoteLog(
        id = logRef.id,
        userId = userId,
        noteId = noteId,
        oldTitle = oldTitle,
        oldContent = oldContent,
        oldGroupName = oldGroupName,
        newTitle = newTitle,
        newContent = newContent,
        newGroupName = newGroupName,
        action = action
    )

    logRef.set(log)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteGroupSelector(
    selectedGroup: String,
    onGroupSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Grupo",
            fontSize = 14.sp,
            color = colorResource(id = R.color.subtitle_color)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            noteGroups.forEach { group ->
                val isSelected = selectedGroup == group

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected)
                                colorResource(id = R.color.notes_accent)
                            else
                                colorResource(id = R.color.category_chip_col)
                        )
                        .clickable { onGroupSelected(group) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = group,
                        fontSize = 13.sp,
                        color = if (isSelected)
                            colorResource(id = R.color.btn_primary_text)
                        else
                            colorResource(id = R.color.category_chip_text)
                    )
                }
            }
        }
    }
}