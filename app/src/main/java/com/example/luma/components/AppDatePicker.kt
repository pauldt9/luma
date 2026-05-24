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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.res.stringResource
import com.example.luma.R

// Despliega un menu de opciones
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
){
    // Determinar si ha sido desplegado el date picker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState() // Guarda la fecha que el usuario ha seleccionado

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        // Label
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
            // Contenedor
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .clickable {
                        showDatePicker = true
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
                    // Fecha seleccionada actualmente
                    Text(
                        text = value.ifEmpty { "DD-MM-AAAA" },
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

            if (showDatePicker){
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    onValueChange(formatDate(millis))
                                }
                                showDatePicker = false // Cierra el calendario
                            }
                        ){
                            Text(stringResource(id = R.string.accept_btn))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ){
                            Text(stringResource(id = R.string.cancel_btn))
                        }
                    }
                ){
                    // Dibuja el calendario
                    DatePicker(
                        state = datePickerState // Guarda la fecha seleccionada dentro del calendario
                    )
                }
            }
        }

    }
}

// Convierte la fecha seleccionada de milisegundos a texto con el formato DD-MM-YYYY
private fun formatDate(millis: Long): String {
    val formatter = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}