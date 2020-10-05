package com.se7en.jetmessenger.ui.screens.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Routing.Root.Main.Story.Content(user: User?) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it }, animSpec = tween(500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it * 2 }, animSpec = tween(500)
        )
    ) {
        user?.let {
            Surface(modifier = Modifier.clickable(onClick = {})) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TopBar(it)
                }
            }
        }
    }
}
