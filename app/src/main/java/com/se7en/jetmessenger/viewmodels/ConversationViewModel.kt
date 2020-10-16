package com.se7en.jetmessenger.viewmodels

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.se7en.jetmessenger.R
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.Emoji
import com.se7en.jetmessenger.data.models.Message
import com.se7en.jetmessenger.data.models.User

class ConversationViewModel : ViewModel() {

    val messages = mutableStateMapOf<User, List<Message>>().withDefault { user ->
        listOf(
            Message(me, "Hey ${user.name.first}"),
            Message(me, "How you doing?"),
            Message(user, "Great! How are you doing?"),
            Emoji(R.drawable.thumbs_up, 40.dp, shouldAnimate = false, user)
        )
    }

    fun sendMessage(to: User, message: String) {
        messages[to] = messages.getValue(to) + Message(me, message)
    }

    fun sendEmoji(size: Dp, to: User, resId: Int) {
        messages[to] = messages.getValue(to) +
                Emoji(resId, size, shouldAnimate = true, me)
    }
}
