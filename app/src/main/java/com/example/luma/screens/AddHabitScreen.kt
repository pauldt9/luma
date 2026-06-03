package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.AppDropdownMenu
import com.example.luma.components.BackButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import com.example.luma.components.CustomButton
import com.example.luma.model.Habit
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

@Composable
fun AddHabitScreen(navController: NavController){
    // Estados para los inputs
    var habitName by remember { mutableStateOf("") }
    var groupName by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Instancias de Firebase
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    // Instancia de CoroutineScope
    val scope = rememberCoroutineScope()

    AppBackground {
        Column {
            // Header
            AddHabitHeader(
                onBackClick = { navController.popBackStack() }
            )

            // Inputs
            AddHabitInputs(
                modifier = Modifier.padding(horizontal = 25.dp),
                habitName = habitName,
                onHabitChange = { habitName = it },
                groupName = groupName,
                onGroupValue = { groupName = it },
                description = description,
                onDescriptionChange = { description = it },
                frequency = frequency,
                onFrequencyValue = { frequency = it }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Boton para agregar el habito
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
                    onClick = {

                        // Valida que los campos no esten vacios
                        if (
                            habitName.isNotBlank() &&
                            description.isNotBlank() &&
                            groupName.isNotBlank() &&
                            frequency.isNotBlank()
                        ) {

                            // Crea un nuevo habito
                            val habitRef =
                                db.collection("habits").document()

                            // Crea un objeto Habit con los datos del habito
                            val habit = Habit(
                                id = habitRef.id,
                                userId = currentUser?.uid ?: "",
                                groupName = groupName,
                                name = habitName,
                                description = description,
                                frequency = frequency
                            )

                            // Guarda el habito en Firestore
                            scope.launch {

                                // Guarda el habito en Firestore
                                habitRef.set(habit)

                                // Regresa a la pantalla anterior
                                navController.popBackStack()
                            }
                        }
                    }
                )
            }
        }
    }
}

// Crea el header de la pantalla de agregar habito
@Composable
private fun AddHabitHeader(
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

        ScreenTitle(stringResource(id = R.string.add_habit_title))
    }
}

// Crea los inputs para agregar un habito
@Composable
private fun AddHabitInputs(
    modifier: Modifier = Modifier,
    habitName: String,
    onHabitChange: (String) -> Unit,
    groupName: String,
    onGroupValue: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
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