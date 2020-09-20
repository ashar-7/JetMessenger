package com.se7en.jetmessenger.viewmodels

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.se7en.jetmessenger.data.models.Message
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.data.me

class ConversationViewModel: ViewModel() {

    val messages = mutableStateMapOf<User, List<Message>>().withDefault { user ->
        listOf(
            Message(me, "Hey ${user.name.first}"),
            Message(me, "How you doing?"),
            Message(user, "Great! How are you doing?")
        )
    }

    fun sendMessage(to: User, message: String) {
        messages[to] = messages.getValue(to) + Message(me, message)
    }
}
