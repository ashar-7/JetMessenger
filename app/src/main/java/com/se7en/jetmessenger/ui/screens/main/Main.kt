package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.github.zsoltk.compose.router.BackStack
import com.github.zsoltk.compose.router.Router
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.screens.story.Content


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Routing.Root.Main.Content(
    onChatClick: (user: User) -> Unit,
    onSearchClick: () -> Unit
) {
    Router(defaultRouting = defaultRouting) { mainBackStack ->
        Router(defaultRouting = Story(visible = false)) { storyBackStack ->

            var selectedStoryUser: User? by remember { mutableStateOf(null) }

            Scaffold(
                topBar = {
                    TopBar(
                        currentRouting = mainBackStack.last(),
                        onActionClick = { }
                    )
                },
                bottomBar = {
                    BottomBar(
                        currentRouting = mainBackStack.last(),
                        onSelected = { onBottomNavItemSelected(it, mainBackStack) }
                    )
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier.padding(innerPadding),
                    color = MaterialTheme.colors.surface
                ) {
                    Crossfade(current = mainBackStack.last()) { routing ->
                        when (routing) {
                            is Routing.BottomNav.Chats -> routing.Content(
                                onChatClick,
                                onSearchClick
                            )
                            is Routing.BottomNav.People -> routing.Content(
                                onChatClick,
                                onSearchClick,
                                { user ->
                                    selectedStoryUser = user
                                    storyBackStack.push(Story(visible = true))
                                }
                            )
                        }
                    }
                }
            }

            storyBackStack.last().Content(
                selectedStoryUser,
                url = "https://picsum.photos/id/${System.currentTimeMillis()%100}/300/300",
                onClose = {
                    storyBackStack.pop()
                }
            )
        }
    }
}

fun onBottomNavItemSelected(routing: Routing.BottomNav, backStack: BackStack<Routing.BottomNav>) {
    when(routing) {
        is Routing.BottomNav.Chats ->
            if(backStack.last() !is Routing.BottomNav.Chats) {
                backStack.newRoot(routing)
            }
        is Routing.BottomNav.People ->
            if(backStack.last() !is Routing.BottomNav.People) {
                backStack.push(routing)
            }
    }
}
