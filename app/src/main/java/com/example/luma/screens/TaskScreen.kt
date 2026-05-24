package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.CardText
import com.example.luma.components.ItemCard
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.example.luma.model.Task
import com.example.luma.model.TaskPriority
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.luma.components.AddFloatingButton


@Composable
fun TaskScreen(navController: NavController){
    MainScaffold(
        selectedItem = "task",
        onBottomItemClick = { route ->
            navController.navigate(route)
        },
        floatingActionButton = {
            AddFloatingButton(
                onClick = {
                    navController.navigate("add_task")
                }
            )
        }
    ) {
        var tasks by remember {
            mutableStateOf(
                // Lista de tareas de prueba
                listOf(
                    Task(1, "Contenido tarea 1", TaskPriority.HIGH, false),
                    Task(2, "Contenido tarea 2", TaskPriority.LOW, true),
                    Task(3, "Contenido tarea 3", TaskPriority.MEDIUM, false)
                )
            )
        }

        TaskHeader(tasks)

        Spacer(modifier = Modifier.height(20.dp))

        TasksList(
            tasks = tasks,
            onEditClick = {task ->
                // Esto es cuando ya este conectado a la base de datos y busque por id
//                navController.navigate("edit_task/${task.id}")
                navController.navigate("edit_task")
            },
            onCheckedChange = { selectedTask, checked ->
                // Crea una nueva lista actualizando solo la tarea seleccionada
                tasks = tasks.map { task ->
                    if (task.id == selectedTask.id) {
                        task.copy(isCompleted = checked)
                    } else {
                        task
                    }
                }
            }
        )



    }
}

/*Titulo y subtitulo. muestra la cantidad de tareas pendientes.
* Recibe la lista de tareas y muestra las tareas pendientes
* Ejemplo: "3" Tareas pendientes
* */
@Composable
private fun TaskHeader(tasks: List<Task>){
    // Solamente cuenta las tareas pendientes
    val pendingTasksCount = tasks.count() {!it.isCompleted}

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(stringResource(id = R.string.task_title))
        ScreenSubtitle(
            stringResource(id = R.string.pending_task_count, pendingTasksCount)
        )
    }
}

// Muestra la lista de tareas
@Composable
private fun TasksList(
    tasks: List<Task>,
    onEditClick: (Task) -> Unit,
    onCheckedChange: (Task, Boolean) -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks){ task ->
            TaskContainer(
                task = task,
                onEditClick = onEditClick,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

// Genera el contenedor, contenido de la tarea, prioridad y checkbox
@Composable
private fun TaskContainer(
    task: Task,
    onEditClick: (Task) -> Unit,
    onCheckedChange: (Task, Boolean) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    ItemCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ){
        Checkbox(
            checked = task.isCompleted, // Estado actual del checkbox
            onCheckedChange = { checked ->
                onCheckedChange(task, checked)
            },
            enabled = true,
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
                text = task.content, // Aqui va el contenido de la tarea
                color = colorResource(id = R.color.text_color)
            )
            PriorityChip(task.priority)
        }

        IconButton(
            onClick = {
                expanded = true // El menu ha sido abierto
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = colorResource(id = R.color.options_icon_col)
            )
        }

        Box {
            // Menu de opciones: Editar y Eliminar
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false // El usuario intento cerrar el menu
                },
                containerColor = colorResource(id = R.color.container_bg),
                tonalElevation = 0.dp,
                shadowElevation = 5.dp
            ) {
                // Editar
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
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete_option),
                            color = colorResource(id = R.color.text_color)
                        )
                    },
                    onClick = {
                        expanded = false

                    }
                )
            }
        }
    }
}

// Agrega "chip" de la prioridad de la tarea
@Composable
private fun PriorityChip(priority: TaskPriority) {
    // Define la prioridad de la tarea
    val priorityText = when (priority) {
        TaskPriority.HIGH -> stringResource(id = R.string.priority_high)
        TaskPriority.MEDIUM -> stringResource(id = R.string.priority_medium)
        TaskPriority.LOW -> stringResource(id = R.string.priority_low)
    }

    // Define el color de acuerdo a la prioridad de la tarea
    val priorityColor = when (priority){
        TaskPriority.HIGH -> colorResource(id = R.color.priotity_high_col)
        TaskPriority.MEDIUM -> colorResource(id = R.color.priority_medium_col)
        TaskPriority.LOW -> colorResource(id = R.color.priority_low_col)
    }

    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = priorityColor
        )
    ) {
        Text(
            text = priorityText,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.white)
        )
    }
}

