package com.example.luma.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.luma.R

// Crear contenedor
@Composable
fun AppCard(
    modifier: Modifier = Modifier, // Permite modificar el tamaño y mas propiedades
    contentPadding: PaddingValues = PaddingValues(16.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    containerColor: Color = colorResource(id = R.color.container_bg),
    containerBorder: Color = colorResource(id = R.color.container_border),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    // Si requiere onClick, entonces
    if (onClick != null) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            ),
            border = BorderStroke(
                width = 1.dp,
                color = containerBorder
            ),
            onClick = onClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalAlignment = horizontalAlignment,
                content = content
            )
        }
    } else { // De no ocupar onClick
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            ),
            border = BorderStroke(
                width = 1.dp,
                color = containerBorder
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalAlignment = horizontalAlignment,
                content = content
            )
        }
    }
}