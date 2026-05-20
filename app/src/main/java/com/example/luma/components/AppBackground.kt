package com.example.luma.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.luma.R

// Fondo principal de TODAS las pantallas
@Composable
fun AppBackground(
    contentAlignment: Alignment = Alignment.TopCenter, // Por defecto esta arriba, es modificable
    content: @Composable BoxScope.() -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bg_color)),
        contentAlignment = contentAlignment,
        content = content
    )
}