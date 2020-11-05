package com.se7en.jetmessenger.data.models

import androidx.compose.ui.unit.Dp

sealed class Message(val from: User, val reactions: ArrayList<Reaction>) {

    class Text(
        val message: String,
        from: User,
        reactions: ArrayList<Reaction> = arrayListOf()
    ) : Message(from, reactions)

    class Emoji(
        val id: String,
        val size: Dp,
        var shouldAnimate: Boolean,
        from: User,
        reactions: ArrayList<Reaction> = arrayListOf()
    ) : Message(from, reactions)
}

data class Reaction(val from: User, val id: String)
