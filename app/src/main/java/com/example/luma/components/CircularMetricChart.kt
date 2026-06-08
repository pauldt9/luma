package com.example.luma.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.luma.R

@Composable
fun CircularMetricChart(
    percentage: Int,
    progressColor: Color = colorResource(id = R.color.prog_bar_line),
    backgroundColor: Color = colorResource(id = R.color.btn_action_background),
    modifier: Modifier = Modifier
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.toFloat(),
        animationSpec = tween(durationMillis = 900),
        label = "CircularMetricAnimation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val strokeWidth = 8.dp.toPx()

            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            // Glow exterior
            drawArc(
                color = progressColor.copy(alpha = 0.01f),
                startAngle = -90f,
                sweepAngle = animatedPercentage * 3.6f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth + 18.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = progressColor.copy(alpha = 0.1f),
                startAngle = -90f,
                sweepAngle = animatedPercentage * 3.6f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth + 10.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Arco principal
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = animatedPercentage * 3.6f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        MetricValue("${animatedPercentage.toInt()}%")
    }
}