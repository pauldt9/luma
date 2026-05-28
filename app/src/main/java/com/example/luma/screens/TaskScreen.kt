package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AddFloatingButton
import com.example.luma.components.CardText
import com.example.luma.components.ItemCard
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.example.luma.model.Task
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskScreen(navController: NavController){
    val db = Firebase.firestore // Instancia de Firestore de Firebase
    val auth = Firebase.auth // Instancia de autenticación de Firebase
    val currentUser = auth.currentUser // Obtiene el usuario actual
    val context = LocalContext.current // Contexto de la aplicación, para mostrar mensajes

    // Lista de tareas
    val tasks = remember { mutableStateListOf<Task>() }

    // Cargar las tareas de Firestore cuando el usuario cambia
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            db.collection("tasks")
                .whereEqualTo("userId", currentUser.uid) // Filtra por el usuario actual según su ID
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        tasks.clear()
                        val items = snapshot.toObjects(Task::class.java)
                        tasks.addAll(items.sortedByDescending { it.timestamp })
                    }
                }
        }
    }

    MainScaffold(
        selectedItem = "task",
        onBottomItemClick = { route ->
            // Navega a la pantalla seleccionada
            navController.navigate(route) {
                // Mantener el estado de la pantalla
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
                    navController.navigate("add_task")
                }
            )
        }
    ) {
        TaskHeader(tasks)
        Spacer(modifier = Modifier.height(20.dp))
        TasksList(
            tasks = tasks,
            onEditClick = { task ->
                // Pasamos el ID de la tarea a la ruta de edición
                navController.navigate("edit_task/${task.id}")
            },
            onDeleteClick = { task ->
                // Borramos la tarea
                db.collection("tasks").document(task.id).delete()
            },
            onCheckedChange = { task, checked ->
                // Actualizamos el estado de la tarea
                db.collection("tasks").document(task.id).update("completed", checked) 
            }
        )
    }
}

// Encabezado de la pantalla de tareas
@Composable
private fun TaskHeader(tasks: List<Task>){
    val pendingTasksCount = tasks.count { !it.completed }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(stringResource(id = R.string.task_title))
        ScreenSubtitle(
            stringResource(id = R.string.pending_task_count, pendingTasksCount)
        )
    }
}

// Lista de tareas
@Composable
private fun TasksList(
    tasks: List<Task>,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    onCheckedChange: (Task, Boolean) -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks, key = { it.id }){ task ->
            TaskContainer(
                task = task,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

// Contenedor de una tarea
@Composable
private fun TaskContainer(
    task: Task,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    onCheckedChange: (Task, Boolean) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    val isOutdated = isDateBeforeToday(task.dueDate) && !task.completed

    ItemCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ){
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onCheckedChange(task, it) },
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.checkbox_checked),
                uncheckedColor = colorResource(id = R.color.checkbox_unchecked),
                checkmarkColor = colorResource(id = R.color.checkbox_checkmark)
            )
        )

        Spacer(Modifier.width(5.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardText(
                text = task.content,
                color = if (isOutdated) Color.Red else colorResource(id = R.color.text_color)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PriorityChip(task.priority)
                if (isOutdated) {
                    OutdatedChip()
                }
            }
        }

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
                        onEditClick(task)
                    }
                )

                // Eliminar
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.delete_option), color = Color.Red) },
                    onClick = {
                        expanded = false
                        onDeleteClick(task)
                    }
                )
            }
        }
    }
}

// Agrega "chip" de la prioridad de la tarea
@Composable
private fun PriorityChip(priority: String) {
    val high = stringResource(id = R.string.priority_high)
    val medium = stringResource(id = R.string.priority_medium)
    val priorityColor = when (priority) {
        high -> colorResource(id = R.color.priotity_high_col)
        medium -> colorResource(id = R.color.priority_medium_col)
        else -> colorResource(id = R.color.priority_low_col)
    }
    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = priorityColor)
    ) {
        Text(
            text = priority,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

// Agrega "chip" de tarea atrasada
@Composable
private fun OutdatedChip() {
    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Red)
    ) {
        Text(
            text = "Atrasada",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

// Comprueba si la fecha de la tarea es anterior a la fecha actual
private fun isDateBeforeToday(dateString: String): Boolean {
    if (dateString.isEmpty()) return false
    return try {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dueDate = sdf.parse(dateString)
        val today = sdf.parse(sdf.format(Date()))
        dueDate?.before(today) ?: false
    } catch (e: Exception) {
        false
    }
}
