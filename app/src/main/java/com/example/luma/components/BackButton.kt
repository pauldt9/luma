package com.example.luma.components

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.colorResource
import com.example.luma.R

@Composable
fun BackButton(
    onClick: () -> Unit
){
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Return",
            tint = colorResource(id = R.color.selected_item)
        )
    }
}