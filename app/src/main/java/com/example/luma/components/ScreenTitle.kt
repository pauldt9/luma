package com.example.luma.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.luma.R
/*
* Formato para los titulos de la App.
* Solamente utilizar para los titulos principales de las pantallas.
*/
@Composable
fun ScreenTitle(text: String){
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(id = R.color.white)
    )
}