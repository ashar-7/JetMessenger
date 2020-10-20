package com.se7en.jetmessenger.viewmodels

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.Message
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.screens.conversation.THUMBS_UP

class ConversationViewModel : ViewModel() {

    val messages = mutableStateMapOf<User, List<Message>>().withDefault { user ->
        listOf(
            Message.Text(message = "Hey ${user.name.first}", from = me),
            Message.Text(message = "How you doing?", from = me),
            Message.Text(message = "Great! How are you doing?", from = user),
            Message.Emoji(THUMBS_UP, 40.dp, shouldAnimate = false, user)
        )
    }

    fun sendTextMessage(to: User, message: String) {
        messages[to] = messages.getValue(to) + Message.Text(message, me)
    }

    fun sendEmoji(to: User, id: String, size: Dp, shouldAnimate: Boolean = true) {
        messages[to] = messages.getValue(to) +
                Message.Emoji(id, size, shouldAnimate, me)
    }
}
