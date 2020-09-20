package com.se7en.jetmessenger.ui.screens.main

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.zsoltk.compose.router.Router
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.ui.components.MainBottomNav
import com.se7en.jetmessenger.ui.components.MainTopBar

@Composable
fun Routing.Root.Main.Content(
    defaultRouting: Routing.BottomNav,
    onChatClick: (user: User) -> Unit
) {
    Router(contextId = "Main", defaultRouting = defaultRouting) { backStack ->
        Scaffold(
            topBar = {
                MainTopBar(
                    me.picture.thumbnail,
                    currentRouting = backStack.last(),
                    onActionClick = { Log.d("MainActivity", "$it") }
                )
            },
            bottomBar = {
                MainBottomNav(
                    listOf(Routing.BottomNav.Chats, Routing.BottomNav.People),
                    currentRouting = backStack.last(),
                    onSelected = { routing ->
                        when(routing) {
                            is Routing.BottomNav.Chats -> backStack.newRoot(routing)
                            is Routing.BottomNav.People -> backStack.push(routing)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Crossfade(current = backStack.last()) { routing ->
                Surface(
                    modifier = Modifier.padding(innerPadding),
                    color = MaterialTheme.colors.surface
                ) {
                    when(routing) {
                        is Routing.BottomNav.Chats -> routing.Content(onChatClick)
                        is Routing.BottomNav.People -> routing.Content(onChatClick)
                    }
                }
            }
        }
    }
}
