package com.example.luma.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.luma.R

/* Agrega el boton flotante (+)
* Uso principal:
* - Agregar TAREA
* - Agregar NOTA
* - Agregar HABITO
*/
@Composable
fun AddFloatingButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = colorResource(id = R.color.container_bg),
        contentColor = colorResource(id = R.color.selected_item)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}