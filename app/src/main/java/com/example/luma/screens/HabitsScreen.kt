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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenTitle
import com.example.luma.model.Habit
import com.example.luma.R
import com.example.luma.components.AddFloatingButton
import com.example.luma.components.AppCard
import com.example.luma.components.CardText
import com.example.luma.components.CardTitle
import com.example.luma.components.ItemCard
import com.example.luma.components.ProgressBar
import com.example.luma.components.ScreenSubtitle
import com.example.luma.model.Task

@Composable
fun HabitsScreen(navController: NavController){
    // Lista de habitos
//    val habits = remember { mutableStateListOf<Habit>() }

    // Lista de ejemplo
    val habits = remember {
        mutableStateListOf(
            Habit(
                id = 1,
                name = "Tomar agua",
                category = "Salud",
                frequency = "Diaria",
                currentStreak = 3,
                bestStreak = 7,
                completedToday = true
            ),
            Habit(
                id = 2,
                name = "Estudiar inglés",
                category = "Estudio",
                frequency = "Diaria",
                currentStreak = 5,
                bestStreak = 10,
                completedToday = true
            ),
            Habit(
                id = 3,
                name = "Leer 10 minutos",
                category = "Personal",
                frequency = "Diaria",
                currentStreak = 1,
                bestStreak = 4,
                completedToday = false
            )
        )
    }

    MainScaffold(
        selectedItem = "habits",
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
                    navController.navigate("add_habit")
                }
            )
        }
    ) {
        HabitsHeader(habits)

        Spacer(modifier = Modifier.height(28.dp))

        HabitsProgressBar(habits)

        Spacer(modifier = Modifier.height(20.dp))

        ScreenSubtitle(stringResource(id = R.string.habits_today_title))

        Spacer(modifier = Modifier.height(15.dp))

        HabitsList(
            habits = habits,
            onEditClick = { navController.navigate("edit_habit") },
            onDeleteClick = {
                // TODO: Borrar habito
            },
            onCheckedChange = { _, _ ->
                // TODO: Agregar funcionalidad
            }
        )
    }
}

// Header
@Composable
fun HabitsHeader(habits: List<Habit>){
    // Busca la racha mas alta de la lista, si la lista esta vacia, utiliza 0
    val currentStreak = habits.maxOfOrNull { it.currentStreak } ?: 0
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(stringResource(id = R.string.habits_title))
        ScreenSubtitle(stringResource(id = R.string.habits_streak, currentStreak))
    }
}

// Body
@Composable
fun HabitsProgressBar(habits: List<Habit>){
    // Cuenta la cantidad de habitos que ya han sido completados
    val completedHabits = habits.count(){ it.completedToday }
    val totalHabits = habits.size

    // Si hay habitos, calcula el progreso del usuario
    val progress = if (totalHabits > 0){
        completedHabits.toFloat() / totalHabits
    } else {
        0f
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Barra de progreso
        AppCard (
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ){
            CardTitle(stringResource(id = R.string.habits_subtitle))

            Spacer(modifier = Modifier.height(23.dp))

            ProgressBar(progress)

            Spacer(modifier = Modifier.height(8.dp))

            CardText(
                text = stringResource(id = R.string.habits_completed, completedHabits, totalHabits),
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun HabitsList(
    habits: List<Habit>,
    onEditClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit,
    onCheckedChange: (Habit, Boolean) -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(habits){ habit ->
            HabitContainer(
                habit = habit,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

// Contenedor de un habito
@Composable
private fun HabitContainer(
    habit: Habit,
    onEditClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit,
    onCheckedChange: (Habit, Boolean) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    ItemCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ){
        Checkbox(
            checked = habit.completedToday,
            onCheckedChange = { onCheckedChange(habit, it) },
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
                text = habit.name,
                color = colorResource(id = R.color.text_color)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryChip(habit.category)

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
                        onEditClick(habit)
                    }
                )

                // Eliminar
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.delete_option), color = Color.Red) },
                    onClick = {
                        expanded = false
                        onDeleteClick(habit)
                    }
                )
            }
        }
    }
}

// Agrega "chip" de la categoria del habito
@Composable
private fun CategoryChip(category: String) {
    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.category_chip_col))
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.category_chip_text)
        )
    }
}