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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.luma.R

@Composable
fun AppCard(
    modifier: Modifier = Modifier, // Permite modificar el tamaño y mas propiedades
    contentPadding: PaddingValues = PaddingValues(16.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.container_bg)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.container_border)
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
}