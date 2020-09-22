package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.ToolbarAction
import com.se7en.jetmessenger.ui.components.CircleImage

@Composable
fun Routing.Root.Main.TopBar(
    currentRouting: Routing.BottomNav,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onActionClick: (action: ToolbarAction) -> Unit = { }
) {
    TopAppBar(
        title = {
            Text(
                text = currentRouting.label,
                style = MaterialTheme.typography.h6
            )
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
fun Routing.Root.Main.BottomBar(
    currentRouting: Routing.BottomNav,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onSelected: (routing: Routing.BottomNav) -> Unit = { }
) {
    BottomNavigation(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = if (isSystemInDarkTheme()) 0.dp else 4.dp
    ) {
        routings.forEach { routing ->
            val selected = routing == currentRouting

            BottomNavigationItem(
                label = {
                    Text(text = routing.label)
                },
                icon = { Icon(routing.icon) },
                selected = selected,
                onClick = { onSelected(routing) },
                unselectedContentColor = EmphasisAmbient.current.disabled.applyEmphasis(
                    contentColor()
                )
            )
        }
    }
}

@Preview
@Composable
fun MainTopBarPreview() {
    Routing.Root.Main().TopBar(Routing.BottomNav.Chats)
}

@Preview
@Composable
fun MainBottomBarPreview() {
    Routing.Root.Main().BottomBar(Routing.BottomNav.Chats)
}
