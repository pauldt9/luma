package com.example.luma.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.luma.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

/**
 * Solo para contenedores.
 * Casos donde utilizar: "Progreso de hoy....... X de X habitos completados" (Pantalla principal)
 */
@Composable
fun CardText(
    text: String,
    color: Color = colorResource(id = R.color.subtitle_color),
    fontSize: TextUnit = 14.sp,
    maxLines: Int = Int.MAX_VALUE, // No hay limite de numero de lineas
    overflow: TextOverflow = TextOverflow.Clip // Si no cabe el texto, lo corta
){
    Text(
        text = text,
        fontSize = fontSize,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Normal,
        color = color,
        maxLines = maxLines,
        overflow = overflow
    )
}