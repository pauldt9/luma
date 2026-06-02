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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HabitsScreen(navController: NavController){

    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    // Lista de habitos
    val habits = remember { mutableStateListOf<Habit>() }

    // Cargar los habitos de Firestore cuando el usuario cambia
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            db.collection("habits")
                .whereEqualTo("userId", currentUser.uid)
                .addSnapshotListener { snapshot, e ->

                    if (e != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        habits.clear()

                        // Obtiene la fecha actual
                        val today = getTodayDate()

                        // Actualiza el habito en Firestore comprobando si ya fue completado hoy
                        val items = snapshot.toObjects(Habit::class.java).map { habit ->
                            if (habit.lastCompletedDate != today && habit.completedToday) {
                                db.collection("habits")
                                    .document(habit.id)
                                    .update("completedToday", false)

                                habit.copy(completedToday = false)
                            } else {
                                habit
                            }
                        }

                        // Ordena la lista por racha
                        habits.addAll(
                            items.sortedByDescending {
                                it.currentStreak
                            }
                        )
                    }
                }
        }
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
            onEditClick = { habit ->
                navController.navigate("edit_habit/${habit.id}")
                          },
            onDeleteClick = { habit ->
                db.collection("habits")
                    .document(habit.id)
                    .delete()
            },
            onCheckedChange = { habit, checked ->

                // Obtiene la fecha actual
                val today = getTodayDate()
                // Obtiene la fecha de ayer
                val yesterday = getYesterdayDate()

                // Si el habito fue marcado como completado hoy
                val newStreak = if (checked) {
                    if (habit.frequency == "Diario") {
                        // Verifica si el habito fue completado ayer
                        when (habit.lastCompletedDate) {
                            today -> habit.currentStreak// Si fue completado hoy, utiliza la racha actual
                            yesterday -> habit.currentStreak + 1// Si lo fue ayer, utiliza la racha actual mas 1
                            else -> 1// Si no fue completado ayer, utiliza 1
                        }
                    } else{
                        habit.currentStreak
                    }
                } else {
                    habit.currentStreak
                }

                // Calcula la racha mas alta
                val bestStreak = maxOf(habit.bestStreak, newStreak)

                // Actualiza el habito en Firestore
                db.collection("habits")
                    .document(habit.id)
                    .update(
                        mapOf(
                            "completedToday" to checked,
                            "currentStreak" to newStreak,
                            "bestStreak" to bestStreak,
                            "lastCompletedDate" to if (checked) today else habit.lastCompletedDate
                        )
                    )
            }
        )
    }
}

// Header
@Composable
fun HabitsHeader(habits: List<Habit>){
    val completedToday = habits.count { it.completedToday }
    val totalHabits = habits.size
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(stringResource(id = R.string.habits_title))
        ScreenSubtitle("Hoy completaste $completedToday de $totalHabits hábitos")
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
                GroupChip(habit.groupName)
                FrequencyChip(habit.frequency)
                if (habit.frequency == "Diario"){
                    StreakChip(habit.currentStreak)
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

// Agrega "chip" del grupo del habito
@Composable
private fun GroupChip(groupName: String) {
    if (groupName.isBlank()) return

    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.category_chip_col)
        )
    ) {
        Text(
            text = groupName,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.category_chip_text)
        )
    }
}

private fun getTodayDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date())
}

private fun getYesterdayDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)

    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(calendar.time)
}

// Agrega "chip" de la frecuencia del habito
@Composable
private fun FrequencyChip(frequency: String) {
    if (frequency.isBlank()) return

    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.category_chip_col)
        )
    ) {
        Text(
            text = frequency,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.category_chip_text)
        )
    }
}

// Agrega "chip" de la racha del habito
@Composable
private fun StreakChip(streak: Int) {
    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.category_chip_col)
        )
    ) {
        Text(
            text = "🔥 $streak días",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.category_chip_text)
        )
    }
}