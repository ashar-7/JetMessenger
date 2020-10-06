package com.se7en.jetmessenger.ui.theme

import androidx.annotation.FloatRange
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.VerticalGradient

@Composable
fun Modifier.verticalGradient(
    colors: List<Color>,
    @FloatRange(from = 0.0, to = 1.0) startYPercentage: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) endYPercentage: Float = 1f,
): Modifier = composed {
    var height by remember { mutableStateOf(0f) }
    val brush = remember(colors, startYPercentage, endYPercentage, height) {
        VerticalGradient(
            colors,
            height * startYPercentage,
            height * endYPercentage
        )
    }

    drawBehind {
        height = size.height
        drawRect(brush)
    }
}