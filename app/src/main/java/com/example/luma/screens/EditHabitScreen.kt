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

@Composable
fun EditHabitScreen(navController: NavController){
    var habitName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }

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
                category = category,
                onCategoryValue = { category = it },
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
                        // TODO: funcionalidad para editar el habito
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

        ScreenTitle(stringResource(id = R.string.add_habit_title))
    }
}

@Composable
private fun EditHabitInputs(
    modifier: Modifier = Modifier,
    habitName: String,
    onHabitChange: (String) -> Unit,
    category: String,
    onCategoryValue: (String) -> Unit,
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

        // Categoria del habito
        AppDropdownMenu(
            label = stringResource(id = R.string.category_lbl),
            value = category,
            options = listOf(
                stringResource(id = R.string.health_category),
                stringResource(id = R.string.study_category),
                stringResource(id = R.string.exercise_category),
                stringResource(id = R.string.wellness_category),
                stringResource(id = R.string.productivity_category),
                stringResource(id = R.string.personal_category)
            ),
            onValueChange = onCategoryValue
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