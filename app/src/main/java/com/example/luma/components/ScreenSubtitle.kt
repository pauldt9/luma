package com.example.luma.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.luma.R

/**
 * Crea los subtitulos para la app, tienen un color que destaca menos.
 * NO utilizar en contenedores.
 * Ejemplo de uso: "Lunes, 11 de mayo, 2026".
 * Nota: consultar Figma para ver el ejemplo para cualquier duda (revisar pantalla principal)
 */
@Composable
fun ScreenSubtitle(text: String){
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = colorResource(id = R.color.subtitle_color)
    )
}