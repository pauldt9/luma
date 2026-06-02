package com.example.luma.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.BackButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import com.example.luma.components.AppDatePicker
import com.example.luma.components.AppDropdownMenu
import com.example.luma.components.CustomButton
import com.example.luma.model.Group
import com.example.luma.model.Task
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

@Composable
fun AddTaskScreen(navController: NavController){
    var taskName by remember { mutableStateOf("") } // Nombre de la tarea
    var priority by remember { mutableStateOf("Baja") } // Prioridad por defecto
    var dueDate by remember { mutableStateOf("") } // Fecha límite

    var groupName by remember { mutableStateOf("") } // Nombre del grupo al que pertenece la tarea
    val groups = remember { mutableStateListOf<Group>() } // Lista de grupos

    var isLoading by remember { mutableStateOf(false) } // Estado de carga

    val context = LocalContext.current // Contexto de la aplicación, para mostrar mensajes
    val auth = Firebase.auth // Instancia de autenticación de Firebase
    val db = Firebase.firestore // Instancia de Firestore de Firebase

    // Cargar los grupos del usuario cuando el usuario cambia
    LaunchedEffect(auth.currentUser) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("groups")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        groups.clear()
                        groups.addAll(snapshot.toObjects(Group::class.java))
                    }
                }
        }
    }

    AppBackground {
        Column {
            // Encabezado de la pantalla
            AddTaskHeader(onBackClick = {navController.popBackStack()})

            // Campos de entrada para la tarea
            AddTaskInputs(
                modifier = Modifier.padding(horizontal = 25.dp),
                taskName = taskName,
                onTaskNameChange = { taskName = it },
                priority = priority,
                onPriorityChange = { priority = it },
                dueDate = dueDate,
                onDueDateChange = { dueDate = it },
                groupName = groupName,
                onGroupChange = { groupName = it }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón para guardar la tarea
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    modifier = Modifier.fillMaxWidth(0.84f),
                    text = stringResource(id = R.string.accept_btn),
                    bgColor = colorResource(id = R.color.btn_action_background),
                    fontColor = colorResource(id = R.color.btn_action_text),
                    isLoading = isLoading,
                    enabled = !isLoading && taskName.isNotEmpty() && groupName.isNotEmpty()
                ) {
                    val userId = auth.currentUser?.uid // Obtiene el ID del usuario autenticado
                    // Si el usuario está autenticado, crea una nueva tarea en Firestore
                    if (userId != null) {
                        isLoading = true
                        val newTaskRef = db.collection("tasks").document()
                        val task = Task(
                            id = newTaskRef.id,
                            userId = userId,
                            groupName = groupName,
                            content = taskName,
                            priority = priority,
                            dueDate = dueDate,
                            completed = false
                        )

                        // Guarda la tarea en Firestore
                        newTaskRef.set(task)
                            .addOnSuccessListener {
                                isLoading = false
                                Toast.makeText(context, "Tarea añadida", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            // Si hay un error al guardar, muestra un mensaje de error
                            .addOnFailureListener { e ->
                                isLoading = false
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Si el usuario no está autenticado, muestra un mensaje de error
                        Toast.makeText(context, "Inicia sesión para añadir tareas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

// Encabezado de la pantalla de añadir tarea
@Composable
private fun AddTaskHeader(
    onBackClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 65.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(
            onClick = onBackClick
        )

        ScreenTitle(stringResource(id = R.string.add_task_title))
    }
}

// Campos de entrada para la tarea
@Composable
private fun AddTaskInputs(
    modifier: Modifier = Modifier,
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    priority: String,
    onPriorityChange: (String) -> Unit,
    dueDate: String,
    onDueDateChange: (String) -> Unit,
    groupName: String,
    onGroupChange: (String) -> Unit
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        // Campo de entrada para el nombre de la tarea
        CustomInput(
            label = stringResource(id = R.string.task_name_input_label),
            value = taskName,
            placeholder = stringResource(id = R.string.task_name_input_placeholder),
            onValueChange = onTaskNameChange,
            inputHeight = 57.dp
        )

        // Menú desplegable para la prioridad de la tarea
        AppDropdownMenu(
            label = stringResource(id = R.string.task_priority_dropdown_label),
            value = priority,
            options = listOf(
                stringResource(id = R.string.priority_high),
                stringResource(id = R.string.priority_medium),
                stringResource(id = R.string.priority_low)
            ),
            onValueChange = onPriorityChange
        )

        // Menú desplegable para el grupo al que pertenece la tarea
        AppDropdownMenu(
            label = "Grupo",
            value = groupName,
            options = listOf(
                "Escuela",
                "Trabajo",
                "Personal",
                "Salud",
                "Ejercicio",
                "Productividad"
            ),
            onValueChange = onGroupChange
        )

        // Selector de fecha para la fecha límite de la tarea
        AppDatePicker(
            label = stringResource(id = R.string.task_due_date_label),
            value = dueDate,
            onValueChange = onDueDateChange
        )
    }
}