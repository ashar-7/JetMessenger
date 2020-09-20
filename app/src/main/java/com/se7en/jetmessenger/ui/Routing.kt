package com.se7en.jetmessenger.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.VectorAsset
import com.se7en.jetmessenger.data.models.User

sealed class Routing {

    sealed class BottomNav(
        val label: String,
        val icon: VectorAsset,
        val actions: List<ToolbarAction>
    ): Routing() {
        object Chats : BottomNav(
            "Chats",
            Icons.Filled.Chat,
            listOf(ToolbarAction.Camera, ToolbarAction.NewMessage)
        )
        object People : BottomNav(
            "People",
            Icons.Filled.People,
            listOf(ToolbarAction.AllPeople, ToolbarAction.AddContacts)
        )
    }

    sealed class Root : Routing() {
        object Main : Root()
        object Search : Root()
        data class Conversation(
            val user: User,
            val actions: List<ToolbarAction> = listOf(
                ToolbarAction.VoiceCall,
                ToolbarAction.VideoCall,
                ToolbarAction.Info
            )
        ) : Root()

        object Settings : Root()
    }
}

sealed class ToolbarAction(val icon: VectorAsset) {
    object Camera : ToolbarAction(Icons.Filled.CameraAlt)
    object NewMessage : ToolbarAction(Icons.Filled.Edit)
    object AllPeople : ToolbarAction(Icons.Filled.People)
    object AddContacts : ToolbarAction(Icons.Filled.Contacts)
    object VoiceCall : ToolbarAction(Icons.Filled.Call)
    object VideoCall : ToolbarAction(Icons.Filled.VideoCall)
    object Info : ToolbarAction(Icons.Filled.Info)
}