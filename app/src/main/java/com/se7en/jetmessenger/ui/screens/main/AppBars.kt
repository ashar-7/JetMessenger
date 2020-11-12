package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.ui.components.CircleImage
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.screens.ToolbarAction

@Composable
fun Routing.Main.TopBar(
    currentRouting: Routing.Main.BottomNav,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onActionClick: (action: ToolbarAction) -> Unit = { }
) {
    TopAppBar(
        title = {
            Text(text = currentRouting.label, style = MaterialTheme.typography.h6)
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                CircleImage(
                    imageData = me.picture.thumbnail,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        actions = {
            currentRouting.actions.forEach { action ->
                IconButton(onClick = { onActionClick(action) }) {
                    Icon(action.icon, tint = contentColor)
                }
            }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = 0.dp
    )
}

@Composable
fun Routing.Main.BottomBar(
    currentRouting: Routing.Main.BottomNav,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onSelected: (routing: Routing.Main.BottomNav) -> Unit = { }
) {
    BottomNavigation(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = if (isSystemInDarkTheme()) 0.dp else 4.dp
    ) {
        bottomNavRoutings.forEach { routing ->
            val selected = routing == currentRouting

            BottomNavigationItem(
                label = { Text(text = routing.label) },
                icon = { Icon(routing.icon) },
                selected = selected,
                onClick = { onSelected(routing) },
                unselectedContentColor = AmbientContentColor.current.copy(alpha = ContentAlpha.disabled)
            )
        }
    }
}

@Preview
@Composable
fun MainTopBarPreview() {
    Routing.Main.TopBar(Routing.Main.BottomNav.Chats)
}

@Preview
@Composable
fun MainBottomBarPreview() {
    Routing.Main.BottomBar(Routing.Main.BottomNav.Chats)
}
