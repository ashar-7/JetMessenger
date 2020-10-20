package com.se7en.jetmessenger.data.models

import androidx.compose.ui.unit.Dp

sealed class Message(val from: User) {

    class Text(val message: String, from: User) : Message(from)

    class Emoji(
        val id: String,
        val size: Dp,
        var shouldAnimate: Boolean,
        from: User
    ) : Message(from)
}
