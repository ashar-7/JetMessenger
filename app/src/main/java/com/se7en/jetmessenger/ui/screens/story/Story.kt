package com.se7en.jetmessenger.ui.screens.story

import androidx.compose.foundation.Text
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.se7en.jetmessenger.ui.Routing

@Composable
fun Routing.Root.Story.Content() {
    Scaffold(
        topBar = {

        },
        bottomBar = {

        }
    ) {
        Text(text = "Story of ${user.name.first}")
    }
}