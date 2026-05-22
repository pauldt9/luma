package com.example.luma.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomButton(
    text: String,
    bgColor: Color,
    fontColor: Color,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
){
    val finalBgColor = if (enabled) bgColor else bgColor.copy(alpha = 0.5f)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(finalBgColor)
            .then(
                if (enabled && !isLoading) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ){
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = fontColor,
                strokeWidth = 2.dp
            )
        } else {
            BasicText(
                text = text,
                style = TextStyle(
                    color = fontColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
