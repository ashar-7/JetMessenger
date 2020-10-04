package com.se7en.jetmessenger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.setContent
import com.github.zsoltk.compose.backpress.AmbientBackPressHandler
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.router.Router
import com.se7en.jetmessenger.ui.screens.conversation.Content
import com.se7en.jetmessenger.ui.screens.main.Content
import com.se7en.jetmessenger.ui.screens.search.Content
import com.se7en.jetmessenger.ui.screens.story.Content
import com.se7en.jetmessenger.ui.theme.JetMessengerTheme

class MainActivity : AppCompatActivity() {

    private val backPressHandler = BackPressHandler()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetMessengerTheme {
                Providers(AmbientBackPressHandler provides backPressHandler) {
                    Root(Routing.Root.Main())
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!backPressHandler.handle()) {
            super.onBackPressed()
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Root(defaultRouting: Routing.Root) {
    Router(defaultRouting = defaultRouting) { backStack ->
        Crossfade(current = backStack.last()) { routing ->
            when(routing) {
                is Routing.Root.Main -> routing.Content(
                    onChatClick = { user ->
                        backStack.push(Routing.Root.Conversation(user))
                    },
                    onSearchClick = { backStack.push(Routing.Root.Search) },
                    onStoryClick = { user ->
                        backStack.push(Routing.Root.Story(user))
                    }
                )
                is Routing.Root.Conversation -> routing.Content(onBackPress = { backStack.pop() })
                is Routing.Root.Story -> routing.Content()
                is Routing.Root.Search -> routing.Content(
                    onBackPress = { backStack.pop() },
                    onUserClick = { user ->
                        backStack.push(Routing.Root.Conversation(user))
                    }
                )
                is Routing.Root.Settings -> Text(text = "settings")
            }
        }
    }
}