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
import androidx.compose.ui.viewinterop.viewModel
import com.github.zsoltk.compose.router.BackStack
import com.github.zsoltk.compose.router.Router
import com.se7en.jetmessenger.data.models.Story
import com.se7en.jetmessenger.data.models.StoryStatus
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.screens.main.story.Content
import com.se7en.jetmessenger.viewmodels.StoryViewModel
import com.se7en.jetmessenger.viewmodels.UsersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun Routing.Root.Main.Content(
    onChatClick: (user: User) -> Unit,
    onSearchClick: () -> Unit
) {
    val viewModel: UsersViewModel = viewModel()
    val storyViewModel: StoryViewModel = viewModel()
    val users: List<User> by viewModel.users.collectAsState()

    Router(defaultRouting = defaultRouting) { mainBackStack ->
        Router(defaultRouting = Story(visible = false)) { storyBackStack ->

            val stories: List<Story> = storyViewModel.getStories(users)
            var currentStory: Story? by remember { mutableStateOf(null) }

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
                                users,
                                onChatClick,
                                onSearchClick
                            )
                            is Routing.BottomNav.People -> routing.Content(
                                users,
                                stories,
                                onChatClick,
                                onSearchClick,
                                onStoryClick = { story ->
                                    currentStory = story
                                    storyBackStack.push(Story(visible = true))
                                }
                            )
                        }
                    }
                }
            }

            storyBackStack.last().Content(
                currentStory,
                onStorySeen = { story ->
                    storyViewModel.updateStoryStatus(story.user, StoryStatus.AVAILABLE_SEEN)
                },
                onNext = { story ->
                    // Get the next story if it exists, otherwise close the story screen
                    val index = stories.indexOf(story)
                    val nextStory = stories.getOrNull(index + 1)
                    if(nextStory != null) currentStory = nextStory else storyBackStack.pop()
                },
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
