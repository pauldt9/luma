package com.example.luma.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma.R

@Composable
fun AppBottomBar(
    selectedItem: String,
    onItemClick: (String) -> Unit
){
    BarContainer {
        BarItem(
            label = stringResource(id = R.string.home_label),
            icon = R.drawable.home_icon,
            selected = selectedItem == "home",
            onClick = { onItemClick("home") }
        )

        BarItem(
            label = stringResource(id = R.string.task_label),
            icon = R.drawable.task_icon,
            selected = selectedItem == "task",
            onClick = { onItemClick("task") }
        )

        BarItem(
            label = stringResource(id = R.string.notes_label),
            icon = R.drawable.notes_icon,
            selected = selectedItem == "notes",
            onClick = { onItemClick("notes") }
        )

        BarItem(
            label = stringResource(id = R.string.habits_label),
            icon = R.drawable.habits_icon,
            selected = selectedItem == "habits",
            onClick = { onItemClick("habits") }
        )

        BarItem(
            label = stringResource(id = R.string.ai_label),
            icon = R.drawable.astroid,
            selected = selectedItem == "ai",
            onClick = { onItemClick("ai") }
        )
    }
}

// Solamente dibuja el contenedor de la barra de navegacion
@Composable
private fun BarContainer(
    content: @Composable RowScope.() -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.bar_bg)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.bar_border)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                10.dp,
                Alignment.CenterHorizontally
            ),
            content = content
        )
    }
}

// Función que crea los botones de la barra de navegacion
@Composable
private fun BarItem(
    label: String,
    icon: Int,
    selected: Boolean,
    onClick: () -> Unit
){
    // Si el icono ha sido seleccionado, entonces el color del icono se vuelve mas visible
    val itemColor = if (selected) {
        colorResource(id = R.color.selected_item)
    } else {
        colorResource(id = R.color.unselected_item)
    }

    // Circulo
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                color = if (selected){
                    colorResource(id = R.color.selected_item_circle)
                } else {
                    Color.Transparent
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Icono y texto
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = itemColor
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = itemColor
            )
        }
    }
}

