package com.se7en.jetmessenger.ui.screens.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.ColumnScope.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
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
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.ToolbarAction
import com.se7en.jetmessenger.ui.components.CircleImage
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis

@Composable
fun Routing.Root.Conversation.TopBar(
    onActionClick: (action: ToolbarAction) -> Unit,
    onBackPress: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.primary,
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleImage(
                    imageData = user.picture.medium,
                    Modifier.size(38.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = user.name.first,
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

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun Routing.Root.Conversation.BottomBar(
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
        var expanded: Boolean by remember { mutableStateOf(false) }

        AnimatedVisibility(visible = expanded) {
            Icon(
                Icons.Rounded.NavigateNext,
                modifier = Modifier
                    .clickable(onClick = { expanded = false })
                    .padding(8.dp, 4.dp, 4.dp, 4.dp)
            )
        }

        AnimatedVisibility(visible = !expanded) {
            Row {
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

        Row(modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurfaceLowEmphasis())
            .weight(1f)
        ) {
            BaseTextField(
                value = message,
                onValueChange = { new ->
                    message = new
                    expanded = true
                },
                modifier = Modifier
                    .padding(14.dp, 4.dp)
                    .weight(1f, true)
                    .align(Alignment.Start),
                keyboardType = KeyboardType.Text,
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary,
                imeAction = ImeAction.NoAction
            )

            Icon(
                Icons.Rounded.EmojiEmotions,
                modifier = Modifier
                    .clickable(onClick = {})
                    .align(Alignment.End)
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
fun ConversationTopBarPreview() {
    Routing.Root.Conversation(me).TopBar(
        {},
        {}
    )
}

@Preview
@Composable
fun ConversationBottomBarPreview() {
    Routing.Root.Conversation(me).BottomBar({})
}
