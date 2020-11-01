package com.se7en.jetmessenger.ui.screens.conversation

import android.annotation.SuppressLint
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.Message
import com.se7en.jetmessenger.data.models.emptyUser
import com.se7en.jetmessenger.ui.backPressHandler
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.screens.ToolbarAction
import com.se7en.jetmessenger.ui.screens.conversation.info.Content
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import com.se7en.jetmessenger.viewmodels.ConversationViewModel
import com.se7en.jetmessenger.viewmodels.UsersViewModel
import dev.chrisbanes.accompanist.coil.CoilImage

val emojiSize = DpPropKey()
val rotation = FloatPropKey()

val minEmojiSize = 20.dp
val maxEmojiSize = 50.dp
const val emojiTimeout = 3000
const val emojiScale = 2f

enum class EmojiState {
    START, END
}

@SuppressLint("Range")
fun createEmojiTransition(): TransitionDefinition<EmojiState> {
    return transitionDefinition {
        state(EmojiState.START) {
            this[emojiSize] = minEmojiSize
            this[rotation] = 0f
        }

        state(EmojiState.END) {
            this[emojiSize] = 0.dp
            this[rotation] = 0f
        }

        transition(EmojiState.START to EmojiState.END) {
            interruptionHandling = InterruptionHandling.SNAP_TO_END

            emojiSize using keyframes {
                durationMillis = emojiTimeout

                // Grow from minEmojiSize to maxEmojiSize until the last 50 ms and shrink to 0 dp in the last 50ms
                minEmojiSize at 0 with LinearOutSlowInEasing
                maxEmojiSize at emojiTimeout - 50 with FastOutSlowInEasing
            }

            rotation using repeatable(
                // 30 iterations of 100 ms = 3 seconds
                iterations = 3000 / 100,
                animation = keyframes {
                    durationMillis = 100
                    -3f at 0
                    3f at 50
                    -3f at 100
                }
            )
        }
    }
}

@Composable
fun Routing.Conversation.Content(
    usersViewModel: UsersViewModel,
    conversationViewModel: ConversationViewModel,
    userId: String,
    onBackPress: () -> Unit
) {
    val user by usersViewModel.getUser(userId).collectAsState(emptyUser())
    val messages = conversationViewModel.messages.getValue(user)

    var themeColor by remember { mutableStateOf(messengerBlue) }
    var currentEmoji by remember { mutableStateOf(THUMBS_UP) }

    var emojiState by remember { mutableStateOf(EmojiState.END) }
    val definition by remember(emojiState) { mutableStateOf(createEmojiTransition()) }
    val transitionState = transition(
        definition = definition,
        initState = emojiState,
        toState = EmojiState.END
    )

    var infoVisible by remember { mutableStateOf(false) }
    backPressHandler(
        enabled = infoVisible,
        onBackPressed = { infoVisible = false }
    )

    Box {
        Scaffold(
            topBar = {
                TopBar(
                    user,
                    onActionClick = { action ->
                        when (action) {
                            ToolbarAction.Info -> infoVisible = true
                            else -> { }
                        }
                    },
                    onBackPress = onBackPress,
                    contentColor = themeColor
                )
            },
            bottomBar = {
                BottomBar(
                    onSendClick = { conversationViewModel.sendTextMessage(user, it) },
                    themeColor = themeColor,
                    emojiId = currentEmoji,
                    onEmojiPressStart = { emojiState = EmojiState.START },
                    onEmojiPressStop = {
                        if (transitionState[emojiSize] > 20.dp) {
                            conversationViewModel.sendEmoji(
                                user,
                                currentEmoji,
                                size = transitionState[emojiSize] * emojiScale,
                            )
                        }
                        emojiState = EmojiState.END
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Messages(
                    messages = messages,
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 16.dp),
                    themeColor = themeColor,
                    emojiId = currentEmoji,
                    transitionState = transitionState,
                    onEmojiAnimationEnd = { it.shouldAnimate = false }
                )
            }
        }

        // TODO: Maybe switch to compose-navigation once it supports transitions?
        Routing.Conversation.Info.Content(
            visible = infoVisible,
            user = user,
            themeColor = themeColor,
            currentEmojiId = currentEmoji,
            onColorSelected = { themeColor = it },
            onEmojiSelected = { currentEmoji = it },
            onBackPress = { infoVisible = !infoVisible }
        )
    }
}

@OptIn(ExperimentalLazyDsl::class)
@Composable
fun Messages(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    themeColor: Color = MaterialTheme.colors.primary,
    emojiId: String,
    transitionState: TransitionState,
    onEmojiAnimationEnd: (emoji: Message.Emoji) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(items = messages) { index, item ->

            when (item) {
                is Message.Emoji -> {
                    EmojiMessageItem(item, themeColor, Modifier.fillMaxWidth(), onEmojiAnimationEnd)
                }
                is Message.Text -> {
                    // Was the previous message NOT from this user?
                    val isFirst = messages.getOrNull(index - 1)?.from != item.from
                    // Is the next message NOT from this user?
                    val isLast = messages.getOrNull(index + 1)?.from != item.from

                    TextMessageItem(item, isFirst, isLast, themeColor, Modifier.fillMaxWidth())
                }
            }
        }

        item {
            // This acts as a placeholder while the emoji button is pressed
            EmojiMessage(
                id = emojiId,
                tint = if(emojiId == THUMBS_UP) themeColor else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 100.dp,
                        end = 12.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    ).wrapContentSize(Alignment.CenterEnd),
                size = transitionState[emojiSize],
                rotation = transitionState[rotation]
            )
        }
    }
}

@Composable
fun TextMessageItem(
    item: Message.Text,
    isFirst: Boolean,
    isLast: Boolean,
    themeColor: Color,
    modifier: Modifier = Modifier
) {
    // The corners towards the user's side of the message (left side for friend, right side for me)
    val topVariedCorner = if (isFirst) 18.dp else 3.dp
    val bottomVariedCorner = if (isLast) 18.dp else 3.dp

    val backgroundColor: Color
    val contentColor: Color
    val messageModifier: Modifier
    when (item.from) {
        me -> {
            backgroundColor = themeColor
            contentColor = Color.White
            messageModifier = Modifier.padding(
                start = 100.dp,
                end = 12.dp,
                top = if (isFirst) 4.dp else 1.dp,
                bottom = if (isLast) 4.dp else 1.dp
            ).wrapContentSize(Alignment.CenterEnd)
        }
        else -> {
            backgroundColor = MaterialTheme.colors.onSurfaceLowEmphasis
            contentColor = contentColorFor(color = backgroundColor)
            messageModifier = Modifier.padding(
                start = 12.dp,
                end = 100.dp,
                top = if (isFirst) 4.dp else 2.dp,
                bottom = if (isLast) 4.dp else 2.dp
            ).wrapContentSize(Alignment.CenterStart)
        }
    }

    TextMessage(
        text = item.message,
        modifier = modifier.then(messageModifier),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        topLeftCorner = if (item.from == me) 18.dp else topVariedCorner,
        topRightCorner = if (item.from == me) topVariedCorner else 18.dp,
        bottomRightCorner = if (item.from == me) bottomVariedCorner else 18.dp,
        bottomLeftCorner = if (item.from == me) 18.dp else bottomVariedCorner
    )
}

@Composable
fun TextMessage(
    text: String,
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    topLeftCorner: Dp = 0.dp,
    topRightCorner: Dp = 0.dp,
    bottomRightCorner: Dp = 0.dp,
    bottomLeftCorner: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topLeftCorner,
                    topRightCorner,
                    bottomRightCorner,
                    bottomLeftCorner
                )
            ).background(backgroundColor)
            .padding(12.dp, 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(
                color = contentColor,
                fontSize = 15.sp
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@Composable
fun EmojiMessageItem(
    item: Message.Emoji,
    themeColor: Color,
    modifier: Modifier,
    onEmojiAnimationEnd: (emoji: Message.Emoji) -> Unit
) {
    val size = if (item.shouldAnimate) {
        // Animate from half the size of the emoji with the spring effect
        val animatedSize = animatedFloat(initVal = item.size.value / 2f)
        onActive {
            animatedSize.animateTo(
                item.size.value,
                spring(dampingRatio = 0.3f), // low damping ratio means more spring
                onEnd = { _, _ -> onEmojiAnimationEnd(item) }
            )
        }
        animatedSize.value.dp
    } else {
        item.size
    }

    EmojiMessage(
        id = item.id,
        tint = if (item.id == THUMBS_UP) themeColor else null,
        size = size,
        rotation = 0f,
        modifier = modifier
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 4.dp,
                bottom = 4.dp
            ).wrapContentSize(if (item.from == me) Alignment.CenterEnd else Alignment.CenterStart)
    )
}

@Composable
fun EmojiMessage(
    id: String,
    size: Dp,
    tint: Color? = null,
    rotation: Float = 0f,
    modifier: Modifier = Modifier,
) {
    CoilImage(
        data = resIdFor(id) ?: "",
        colorFilter = tint?.let { ColorFilter.tint(it) },
        modifier = modifier
            .size(size)
            .drawLayer(rotationZ = rotation)
    )
}
