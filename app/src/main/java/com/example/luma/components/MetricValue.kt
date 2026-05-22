package com.example.luma.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.luma.R

/**
 * Solo se utiliza en el HomeScreen.
 */
@Composable
fun MetricValue(text: String){
    Text(
        text = text,
        fontSize = 36.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(id = R.color.text_color)
    )
}