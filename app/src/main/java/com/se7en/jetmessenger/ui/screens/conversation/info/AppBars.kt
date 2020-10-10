package com.se7en.jetmessenger.ui.screens.conversation.info

import androidx.compose.foundation.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.se7en.jetmessenger.ui.Routing

@Composable
fun Routing.Root.Conversation.Info.TopBar(
    onBackPress: () -> Unit,
    onMoreClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(Icons.Rounded.ArrowBack)
            }
        },
        actions = {
            IconButton(onClick = onMoreClick) {
                Icon(Icons.Rounded.MoreVert)
            }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = 0.dp
    )
}