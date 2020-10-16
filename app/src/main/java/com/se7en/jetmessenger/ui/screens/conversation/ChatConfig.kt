package com.se7en.jetmessenger.ui.screens.conversation

import androidx.compose.ui.graphics.Color
import com.se7en.jetmessenger.ui.theme.messengerBlue

val themeColors = listOf(
    messengerBlue,
    Color(red = 0, green = 132, blue = 255),
    Color(red = 68, green = 191, blue = 199),
    Color(red = 255, green = 195, blue = 0),
    Color(red = 251, green = 60, blue = 76),
    Color(red = 214, green = 50, blue = 187),
    Color(red = 102, green = 154, blue = 204),
    Color(red = 18, green = 207, blue = 19),
    Color(red = 255, green = 126, blue = 42),
    Color(red = 231, green = 133, blue = 134),
    Color(red = 118, green = 70, blue = 254),
    Color(red = 32, green = 205, blue = 245),
    Color(red = 103, green = 184, blue = 105),
    Color(red = 212, green = 168, blue = 141),
    Color(red = 255, green = 92, blue = 161),
    Color(red = 166, green = 150, blue = 199),
)

fun <T> List<T>.toGridList(cols: Int): List<List<T?>> =
    chunked(cols).map { row ->
        // Fill list with null values so that size == cols
        row.plus(List(size = cols - row.size) { null })
    }
