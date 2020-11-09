package com.se7en.jetmessenger.ui.screens.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.Message
import com.se7en.jetmessenger.data.models.ReplyTo
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.components.CircleImage
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.screens.ToolbarAction
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Routing.Conversation.TopBar(
    user: User,
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
        elevation = if (isSystemInDarkTheme()) 0.dp else 4.dp
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Routing.Conversation.BottomBar(
    emojiId: String,
    replyingTo: ReplyTo? = null,
    themeColor: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.surface,
    onReplyingToBoxClose: () -> Unit,
    onSendClick: (text: String) -> Unit,
    onEmojiPressStart: () -> Unit,
    onEmojiPressStop: () -> Unit
) {
    Surface(elevation = if (isSystemInDarkTheme()) 0.dp else 4.dp) {
        Column {
            AnimatedVisibility(visible = replyingTo != null) {
                replyingTo?.let {
                    ReplyingTo(
                        replyingTo = it,
                        themeColor = themeColor,
                        onClose = onReplyingToBoxClose,
                    )
                }
            }

            BottomAppBar(
                backgroundColor = backgroundColor,
                contentColor = themeColor,
                elevation = 0.dp
            ) {
                BottomBarLowerContent(
                    emojiId = emojiId,
                    themeColor = themeColor,
                    onSendClick = onSendClick,
                    onEmojiPressStart = onEmojiPressStart,
                    onEmojiPressStop = onEmojiPressStop
                )
            }
        }
    }
}

@Composable
private fun ReplyingTo(
    replyingTo: ReplyTo,
    themeColor: Color,
    onClose: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp, 8.dp)
        ) {
            Text(
                text = "Replying to ${replyingTo.name}",
                style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp)
            )

            when(val message = replyingTo.message) {
                is Message.Emoji -> {
                    val tint =
                        if (message.id == THUMBS_UP) ColorFilter.tint(themeColor)
                        else null

                    CoilImage(
                        data = resIdFor(message.id) ?: "",
                        colorFilter = tint,
                        modifier = Modifier.size(16.dp)
                    )
                }
                is Message.Text -> {
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }

        Icon(
            asset = Icons.Rounded.Close,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .size(16.dp)
                .clickable(onClick = onClose)
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
private fun RowScope.BottomBarLowerContent(
    emojiId: String,
    themeColor: Color,
    onSendClick: (text: String) -> Unit,
    onEmojiPressStart: () -> Unit,
    onEmojiPressStop: () -> Unit
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

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurfaceLowEmphasis)
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
                .weight(1f, true),
            keyboardType = KeyboardType.Text,
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.primary,
            imeAction = ImeAction.NoAction
        )

        Icon(
            Icons.Rounded.EmojiEmotions,
            modifier = Modifier
                .clickable(onClick = {})
                .padding(4.dp, 4.dp)
        )
    }

    when (message.text.isNotEmpty()) {
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
            IconButton(onClick = {}) {
                CoilImage(
                    data = resIdFor(emojiId) ?: "",
                    colorFilter = if(emojiId == THUMBS_UP) ColorFilter.tint(themeColor) else null,
                    modifier = Modifier
                        .pressIndicatorGestureFilter(
                            onStart = { onEmojiPressStart() },
                            onStop = onEmojiPressStop,
                            onCancel = onEmojiPressStop
                        ).padding(10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ConversationTopBarPreview() {
    Routing.Conversation.TopBar(
        me,
        {},
        {}
    )
}

@Preview
@Composable
fun ConversationBottomBarPreview() {
    Routing.Conversation.BottomBar(
        THUMBS_UP,
        replyingTo = ReplyTo(me.name.first, Message.Text("Hey", me)),
        onReplyingToBoxClose = {},
        onSendClick = {},
        onEmojiPressStart = {},
        onEmojiPressStop = {}
    )
}
