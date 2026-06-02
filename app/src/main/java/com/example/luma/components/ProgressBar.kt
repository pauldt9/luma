package com.example.luma.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.luma.R

// Barra de progreso para
@Composable
fun ProgressBar(
    progress: Float, // Progreso de la barra
    modifier: Modifier = Modifier
){
    LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) }, // Evita valores invalidos
        modifier = modifier
            .fillMaxWidth()
            .height(9.dp)
            .clip(RoundedCornerShape(50)),
        color = colorResource(id = R.color.prog_bar_line),
        trackColor = colorResource(id = R.color.prog_bar_bg),
        drawStopIndicator = {},
        gapSize = 0.dp
    )
}