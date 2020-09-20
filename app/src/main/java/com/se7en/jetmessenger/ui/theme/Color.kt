package com.se7en.jetmessenger.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val messengerBlue = Color(0xFF0078FF)
val messengerLightBlue = Color(0xFF00C6FF)

fun Colors.onSurfaceLowEmphasis(): Color {
    return onSurface.copy(alpha = 0.12f)
}
