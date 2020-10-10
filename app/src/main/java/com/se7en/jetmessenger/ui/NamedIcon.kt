package com.se7en.jetmessenger.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VideoCall
import androidx.compose.ui.graphics.vector.VectorAsset

sealed class NamedIcon(val name : String, val icon : VectorAsset) {
    object Audio : NamedIcon("Audio", Icons.Rounded.Call)
    object Video : NamedIcon("Video", Icons.Rounded.VideoCall)
    object Profile : NamedIcon("Profile", Icons.Rounded.Person)
    object Mute : NamedIcon("Mute", Icons.Rounded.Notifications)
}
