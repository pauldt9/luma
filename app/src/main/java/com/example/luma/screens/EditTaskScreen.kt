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
import com.example.luma.components.AppDatePicker
import com.example.luma.components.AppDropdownMenu
import com.example.luma.components.BackButton
import com.example.luma.components.CustomButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import com.example.luma.model.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun EditTaskScreen(navController: NavController, taskId: String){
    // Estado para los datos de la tarea
    var taskName by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // Contexto para mostrar mensajes
    val context = LocalContext.current
    // Instancia de Firestore
    val db = Firebase.firestore

    // Cargar datos actuales de la tarea
    LaunchedEffect(taskId) {
        if (taskId.isNotEmpty()) {
            isLoading = true // Mostrar indicador de carga
            // Obtener datos de la tarea
            db.collection("tasks").document(taskId).get()
                .addOnSuccessListener { document ->
                    val task = document.toObject(Task::class.java) // Convertir a objeto Task
                    // Actualizar los campos de entrada
                    if (task != null) {
                        taskName = task.content
                        priority = task.priority
                        dueDate = task.dueDate
                    }
                    isLoading = false // Ocultar indicador de carga
                }
                // Manejo de errores
                .addOnFailureListener {
                    isLoading = false
                    Toast.makeText(context, "Error al cargar la tarea", Toast.LENGTH_SHORT).show()
                }
        }
    }

    AppBackground()  {
        if (isLoading) {
            // Poner un indicador de carga aquí
        }

        // Contenido de la pantalla de edición de tarea
        Column {
            EditTaskHeader(onBackClick = {navController.popBackStack()})

            // Campos de entrada para la tarea en la pantalla de edición
            EditTaskInputs(
                modifier = Modifier.padding(horizontal = 25.dp),
                taskName = taskName,
                onTaskNameChange = { taskName = it },
                priority = priority,
                onPriorityChange = { priority = it },
                dueDate = dueDate,
                onDueDateChange = { dueDate = it }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Boton guardar cambios
                CustomButton(
                    modifier = Modifier.fillMaxWidth(0.84f),
                    text = stringResource(id = R.string.save_btn),
                    bgColor = colorResource(id = R.color.btn_action_background),
                    fontColor = colorResource(id = R.color.btn_action_text),
                    isLoading = isSaving, // Mostrar indicador de carga
                    enabled = !isSaving && taskName.isNotEmpty() // Boton habilitado
                ) {
                    isSaving = true // Mostrar indicador de carga
                    val updates = mapOf( // Actualizar campos en Firestore
                        "content" to taskName,
                        "priority" to priority,
                        "dueDate" to dueDate
                    )

                    // Actualizar la tarea en Firestore
                    db.collection("tasks").document(taskId).update(updates)
                        .addOnSuccessListener {
                            isSaving = false
                            Toast.makeText(context, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                        .addOnFailureListener { e ->
                            isSaving = false
                            Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}

@Composable
private fun EditTaskHeader(
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

        ScreenTitle(stringResource(id = R.string.edit_task_title))
    }
}

// Funcion para los campos de entrada de la tarea en la pantalla de edición
@Composable
private fun EditTaskInputs(
    modifier: Modifier = Modifier,
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    priority: String,
    onPriorityChange: (String) -> Unit,
    dueDate: String,
    onDueDateChange: (String) -> Unit
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        CustomInput(
            label = stringResource(id = R.string.task_name_input_label),
            value = taskName,
            placeholder = stringResource(id = R.string.task_name_input_placeholder),
            onValueChange = onTaskNameChange,
            isPassword = false,
            inputHeight = 57.dp
        )

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

        AppDatePicker(
            label = stringResource(id = R.string.task_due_date_label),
            value = dueDate,
            onValueChange = onDueDateChange
        )
    }
}