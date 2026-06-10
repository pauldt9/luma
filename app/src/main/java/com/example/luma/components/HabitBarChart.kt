package com.example.luma.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma.R

@Composable
fun HabitBarChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier,
    barColor: Color = colorResource(id = R.color.habits_accent)
) {
    val maxValue = data.values.maxOrNull()?.coerceAtLeast(1) ?: 1

    val barColors = listOf(
        colorResource(id = R.color.habits_accent),      // Verde
        colorResource(id = R.color.task_accent),        // Azul
        colorResource(id = R.color.notes_accent),       // Morado
        colorResource(id = R.color.productivity_accent),// Naranja
        colorResource(id = R.color.ai_accent)           // Cyan
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        data.entries.forEachIndexed { index, entry ->

            val group = entry.key
            val value = entry.value

            val currentColor = barColors[index % barColors.size]

            val progress by animateFloatAsState(
                targetValue = value.toFloat() / maxValue.toFloat(),
                animationSpec = tween(durationMillis = 800),
                label = "HabitBarProgress"
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(currentColor)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = group,
                            color = colorResource(id = R.color.text_color),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = "$value",
                        color = currentColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(colorResource(id = R.color.prog_bar_bg))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .background(currentColor)
                    )
                }
            }
        }
    }
}