package com.example.luma.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma.R

// Despliega un menu de opciones
@Composable
fun AppDropdownMenu(
    label: String,
    value: String,
    options: List<String>, // Insertar aqui las opciones que estaran en el menu
    onValueChange: (String) -> Unit
){
    // Determinar si ha sido desplegado el menu
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        // Nombre del dropdown
        BasicText(
            text = label,
            style = TextStyle(
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            // Contenedor del dropdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .clickable {
                        expanded = true
                    },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.textfield_bg_col)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = colorResource(id = R.color.textfield_border_col)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp)
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Valor actual del dropdown
                    Text(
                        text = value,
                        color = colorResource(id = R.color.text_color),
                        fontSize = 14.sp
                    )

                    // Flecha
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Open menu",
                        tint = colorResource(id = R.color.text_color)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded, // Decide si el menu esta abierto o cerrado
                onDismissRequest = {
                    expanded = false // Se cierra cuando se toca fuera del menu
                },
                containerColor = colorResource(id = R.color.container_bg)
            ) {
                // Recorre las opciones del menu y crea los items que estaran dentro del dropdown
                options.forEach {option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                color = colorResource(id = R.color.text_color)
                            )
                        },
                        onClick = {
                            onValueChange(option) // Envia la opcion seleccionada al componente padre
                            expanded = false // Cierra el dropdown al seleccionar una opcion
                        }
                    )
                }
            }
        }

    }
}