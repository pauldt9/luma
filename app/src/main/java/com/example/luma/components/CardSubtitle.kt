package com.example.luma.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.luma.R

/**
 * Solo para contenedores.
 * Casos donde utilizar: "Progreso de hoy....... X de X habitos completados" (Pantalla principal)
 */
@Composable
fun CardSubtitle(text: String){
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = colorResource(id = R.color.subtitle_color)
    )
}