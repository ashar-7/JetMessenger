package com.se7en.jetmessenger.data.models

import androidx.compose.ui.unit.Dp

// TODO: make a sealed class for Message and inherit Text and Emoji from them

open class Message(
    val from: User,
    val message: String
)

class Emoji(
    val resId: Int,
    val size: Dp,
    var shouldAnimate: Boolean,
    from: User
) : Message(from, "")