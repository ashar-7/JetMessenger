package com.se7en.jetmessenger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.screens.conversation.Content
import com.se7en.jetmessenger.ui.screens.main.Content
import com.se7en.jetmessenger.ui.screens.search.Content
import com.se7en.jetmessenger.ui.theme.JetMessengerTheme
import com.se7en.jetmessenger.viewmodels.ConversationViewModel
import com.se7en.jetmessenger.viewmodels.UsersViewModel

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Providers(AmbientBackPressedDispatcher provides this) {
                JetMessengerTheme {
                    Root()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Root() {
    val navController = rememberNavController()
    val usersViewModel: UsersViewModel = viewModel()
    val conversationViewModel: ConversationViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routing.Main.route) {
        composable(Routing.Main.route) {
            Routing.Main.Content(
                usersViewModel,
                onChatClick = { user ->
                    navController.navigate("${Routing.Conversation.route}/${user.id}")
                },
                onSearchClick = { navController.navigate(Routing.Search.route) }
            )
        }

        composable(
            "${Routing.Conversation.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("userId")?.let { userId ->
                Routing.Conversation.Content(
                    usersViewModel,
                    conversationViewModel,
                    userId = userId,
                    onBackPress = { navController.popBackStack() }
                )
            }
        }

        composable(Routing.Search.route) {
            Routing.Search.Content(
                usersViewModel,
                onBackPress = { navController.popBackStack() },
                onUserClick = { user ->
                    navController.navigate("${Routing.Conversation.route}/${user.id}")
                }
            )
        }

        composable(Routing.Settings.route) {}
    }
}