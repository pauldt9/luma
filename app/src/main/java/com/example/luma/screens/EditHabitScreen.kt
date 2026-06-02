package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.AppDropdownMenu
import com.example.luma.components.BackButton
import com.example.luma.components.CustomButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import androidx.compose.runtime.LaunchedEffect
import com.example.luma.model.Habit
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

@Composable
fun EditHabitScreen(
    navController: NavController,
    habitId: String
){
    // Estados para los inputs
    var habitName by remember { mutableStateOf("") }
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }

    // Instancias de Firebase
    val db = Firebase.firestore

    // Carga los datos del habito
    LaunchedEffect(habitId) {

        db.collection("habits")
            .document(habitId)
            .get()
            .addOnSuccessListener { document ->

                val habit = document.toObject(Habit::class.java)

                if (habit != null) {
                    habitName = habit.name
                    description = habit.description
                    groupName = habit.groupName
                    frequency = habit.frequency
                }
            }
    }

    AppBackground {
        Column {
            // Header
            EditHabitHeader(
                onBackClick = { navController.popBackStack() }
            )

            // Inputs
            EditHabitInputs(
                modifier = Modifier.padding(horizontal = 25.dp),
                habitName = habitName,
                onHabitChange = { habitName = it },
                description = description,
                onDescriptionChange = { description = it },
                groupName = groupName,
                onGroupValue = { groupName = it },
                frequency = frequency,
                onFrequencyValue = { frequency = it }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Boton para editar el habito
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    modifier = Modifier.fillMaxWidth(0.84f),
                    text = stringResource(id = R.string.save_btn),
                    bgColor = colorResource(id = R.color.btn_action_background),
                    fontColor = colorResource(id = R.color.btn_action_text),
                    onClick = {

                        // Valida que los campos no esten vacios
                        if (
                            habitName.isNotBlank() &&
                            description.isNotBlank() &&
                            groupName.isNotBlank() &&
                            frequency.isNotBlank()
                        ) {
                            // Actualiza el habito
                            val updates = mapOf(
                                "name" to habitName,
                                "description" to description,
                                "groupName" to groupName,
                                "frequency" to frequency
                            )

                            // Actualiza el habito en Firestore
                            db.collection("habits")
                                .document(habitId)
                                .update(updates)
                                .addOnSuccessListener {
                                    navController.popBackStack()
                                }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun EditHabitHeader(
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

        ScreenTitle(stringResource(id = R.string.edit_habit_title))
    }
}

@Composable
private fun EditHabitInputs(
    modifier: Modifier = Modifier,
    habitName: String,
    onHabitChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    groupName: String,
    onGroupValue: (String) -> Unit,
    frequency: String,
    onFrequencyValue: (String) -> Unit
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ){
        // Nombre del habito
        CustomInput(
            label = stringResource(id = R.string.habit_name_lbl),
            value = habitName,
            placeholder = stringResource(id = R.string.habit_name_placeholder),
            onValueChange = onHabitChange,
            inputHeight = 57.dp
        )

        // Descripcion del habito
        CustomInput(
            label = "Descripción",
            value = description,
            placeholder = "Ej. Tomar 2 litros de agua",
            onValueChange = onDescriptionChange,
            inputHeight = 57.dp
        )

        // Grupo del habito
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
            onValueChange = onGroupValue
        )

        // Frecuencia del habito
        AppDropdownMenu(
            label = stringResource(id = R.string.frequency_lbl),
            value = frequency,
            options = listOf(
                stringResource(id = R.string.daily_frec),
                stringResource(id = R.string.weekly_frec),
                stringResource(id = R.string.monthly_frec)
            ),
            onValueChange = onFrequencyValue
        )
    }
}