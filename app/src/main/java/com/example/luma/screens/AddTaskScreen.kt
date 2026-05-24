package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.BackButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import com.example.luma.components.AppDatePicker
import com.example.luma.components.AppDropdownMenu
import com.example.luma.components.CustomButton

@Composable
fun AddTaskScreen(navController: NavController){
    var taskName by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Baja") } // Prioridad por defecto
    var dueDate by remember { mutableStateOf("") }

    AppBackground {
        Column {
            AddTaskHeader(onBackClick = {navController.popBackStack()})

            AddTaskInputs(
                modifier = Modifier.padding(horizontal = 25.dp),
                taskName = taskName,
                onTaskNameChange = { newValue ->
                    taskName = newValue // Guarda el valor que el usuario ha ingresado al input
                },
                priority = priority,
                onPriorityChange = { newValue ->
                    priority = newValue // Guarda la prioridad que el usuario ha seleccionado
                },
                dueDate = dueDate,
                onDueDateChange = { selectedDate ->
                    dueDate = selectedDate // Guarda la fecha seleccionada por el usuario
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Boton aceptar
                CustomButton(
                    modifier = Modifier.fillMaxWidth(0.84f),
                    text = stringResource(id = R.string.accept_btn),
                    bgColor = colorResource(id = R.color.btn_action_background),
                    fontColor = colorResource(id = R.color.btn_action_text)
                ) {
                    // TODO: Guardar la informacion ingresada por el usuario
                    navController.popBackStack()
                }
            }
        }
    }
}

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

@Composable
private fun AddTaskInputs(
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