package com.se7en.jetmessenger.data.models

import androidx.compose.ui.unit.Dp

sealed class Message(
    val from: User,
    val reactions: ArrayList<Reaction>,
    val repliedTo: ReplyTo? = null
) {

    class Text(
        val message: String,
        from: User,
        reactions: ArrayList<Reaction> = arrayListOf(),
        repliedTo: ReplyTo? = null,
    ) : Message(from, reactions, repliedTo)

    class Emoji(
        val id: String,
        val size: Dp,
        var shouldAnimate: Boolean,
        from: User,
        reactions: ArrayList<Reaction> = arrayListOf(),
        repliedTo: ReplyTo? = null
    ) : Message(from, reactions, repliedTo)
}

data class Reaction(val from: User, val id: String)
data class ReplyTo(val name: String, val message: Message)