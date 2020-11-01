package com.se7en.jetmessenger.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.People
import androidx.compose.ui.graphics.vector.VectorAsset

sealed class Routing(val route: String, val label: String) {

    object Main : Routing(route = "main", label = "Main") {
        object Story

        val bottomNavRoutings = listOf(BottomNav.Chats, BottomNav.People)

        sealed class BottomNav(
            route: String,
            label: String,
            val icon: VectorAsset,
            val actions: List<ToolbarAction>,
        ): Routing(route, label) {
            object Chats : BottomNav(
                route = "chats",
                label = "Chats",
                Icons.Rounded.Chat,
                listOf(ToolbarAction.Camera, ToolbarAction.NewMessage)
            )

            object People : BottomNav(
                route = "people",
                label = "People",
                Icons.Rounded.People,
                listOf(ToolbarAction.AllPeople, ToolbarAction.AddContacts)
            )
        }
    }

    object Conversation : Routing(route = "conversation", label = "Conversation") {
        object Info

        val actions: List<ToolbarAction> = listOf(
            ToolbarAction.VoiceCall,
            ToolbarAction.VideoCall,
            ToolbarAction.Info
        )
    }

    object Search : Routing(route = "search", label = "Search")
    object Settings : Routing(route = "settings", label = "Settings")
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