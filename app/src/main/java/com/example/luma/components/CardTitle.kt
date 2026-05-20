package com.example.luma.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.luma.R

/**
 * Titulo SOLO para contenedores.
 * Casos donde utilizar: "Notas", "Tareas", "Habitos".... (revisar Figma de la pantalla principal)
 */

@Composable
fun CardTitle(text: String){
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(id = R.color.text_color)
    )
}