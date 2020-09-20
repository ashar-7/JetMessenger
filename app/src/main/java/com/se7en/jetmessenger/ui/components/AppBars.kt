package com.se7en.jetmessenger.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.ToolbarAction
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis

@Composable
fun MainTopBar(
    profileImage: Any,
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
                    imageData = profileImage,
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
fun ConversationTopBar(
    title: String,
    profileImage: Any,
    actions: List<ToolbarAction>,
    onActionClick: (action: ToolbarAction) -> Unit,
    onBackPress: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.primary,
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleImage(imageData = profileImage, Modifier.size(38.dp))
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(Icons.Rounded.ArrowBack)
            }
        },
        actions = {
            actions.forEach { action ->
                IconButton(onClick = { onActionClick(action) }) {
                    Icon(action.icon, tint = contentColor)
                }
            }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = if(isSystemInDarkTheme()) 0.dp else 4.dp
    )
}

@Composable
fun MainBottomNav(
    routings: List<Routing.BottomNav>,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationBottomBar(
    onSendClick: (text: String) -> Unit,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.primary,
) {
    BottomAppBar(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = if(isSystemInDarkTheme()) 0.dp else 4.dp
    ) {
        var message: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

        when(message.text.isNotEmpty()) {
            true -> {
                Icon(
                    Icons.Rounded.NavigateNext,
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(8.dp, 4.dp, 4.dp, 4.dp)
                )
            }
            false -> {
                Icon(
                    Icons.Rounded.ViewModule,
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(8.dp, 4.dp, 4.dp, 4.dp)
                )

                Icon(
                    Icons.Rounded.CameraAlt,
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(4.dp)
                )

                Icon(
                    Icons.Rounded.Image,
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(4.dp)
                )

                Icon(
                    Icons.Rounded.Mic,
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(4.dp, 4.dp, 8.dp, 4.dp)
                )
            }
        }

        // TODO: Expand Animation
        Stack(modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurfaceLowEmphasis())
            .weight(1f)
        ) {
            BaseTextField(
                value = message,
                onValueChange = { new ->
                    message = new
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(14.dp, 4.dp)
                    .align(Alignment.Center),
                keyboardType = KeyboardType.Text,
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.onSurface,
                imeAction = ImeAction.NoAction
            )

            Icon(
                Icons.Rounded.EmojiEmotions,
                modifier = Modifier
                    .clickable(onClick = {})
                    .align(Alignment.CenterEnd)
                    .padding(4.dp, 4.dp)
            )
        }

        when(message.text.isNotEmpty()) {
            true -> {
                Icon(
                    Icons.Rounded.Send,
                    modifier = Modifier
                        .clickable(onClick = {
                            onSendClick(message.text)
                            message = TextFieldValue("")
                        })
                        .padding(8.dp, 4.dp)
                )
            }
            false -> {
                Icon(
                    Icons.Rounded.ThumbUpAlt,
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(8.dp, 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun MainTopBarPreview() {
    MainTopBar(me.picture.medium, Routing.BottomNav.Chats)
}

@Preview
@Composable
fun ConversationTopBarPreview() {
    ConversationTopBar(
        "John",
        "",
        listOf(ToolbarAction.VoiceCall, ToolbarAction.VideoCall),
        {},
        {}
    )
}

@Preview
@Composable
fun MainBottomBarPreview() {
    MainBottomNav(
        listOf(
            Routing.BottomNav.Chats,
            Routing.BottomNav.People
        ),
        Routing.BottomNav.Chats
    )
}

@Preview
@Composable
fun ConversationBottomBarPreview() {
    ConversationBottomBar({})
}
