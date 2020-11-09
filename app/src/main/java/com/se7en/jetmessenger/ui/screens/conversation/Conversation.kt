package com.se7en.jetmessenger.ui.screens.conversation

import android.annotation.SuppressLint
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Reply
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.*
import com.se7en.jetmessenger.ui.backPressHandler
import com.se7en.jetmessenger.ui.components.CircleImage
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.screens.ToolbarAction
import com.se7en.jetmessenger.ui.screens.conversation.info.Content
import com.se7en.jetmessenger.ui.theme.messageBackground
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import com.se7en.jetmessenger.viewmodels.ConversationViewModel
import com.se7en.jetmessenger.viewmodels.UsersViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.math.abs

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
    var replyingTo by remember { mutableStateOf<ReplyTo?>(null) }

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
                    emojiId = currentEmoji,
                    replyingTo = replyingTo,
                    themeColor = themeColor,
                    onReplyingToBoxClose = { replyingTo = null },
                    onSendClick = {
                        conversationViewModel.sendTextMessage(user, it, replyingTo)
                        replyingTo = null
                    },
                    onEmojiPressStart = { emojiState = EmojiState.START },
                    onEmojiPressStop = {
                        if (transitionState[emojiSize] > 20.dp) {
                            conversationViewModel.sendEmoji(
                                user,
                                currentEmoji,
                                size = transitionState[emojiSize] * emojiScale,
                                repliedTo = replyingTo
                            )
                            replyingTo = null
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
                    user = user,
                    viewModel = conversationViewModel,
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp),
                    themeColor = themeColor,
                    emojiId = currentEmoji,
                    transitionState = transitionState,
                    onReplyDragStopped = { message ->
                        val name = if(message.from == me) "Yourself" else message.from.name.first
                        replyingTo = ReplyTo(name, message)
                    },
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
    user: User,
    viewModel: ConversationViewModel,
    modifier: Modifier = Modifier,
    themeColor: Color = MaterialTheme.colors.primary,
    emojiId: String,
    transitionState: TransitionState,
    onReplyDragStopped: (message: Message) -> Unit,
    onEmojiAnimationEnd: (emoji: Message.Emoji) -> Unit
) {
    val messages = viewModel.messages.getValue(user)

    LazyColumn(modifier = modifier.animateContentSize()) {
        itemsIndexed(items = messages) { index, item ->

            // Was the previous message NOT from this user?
            val isFirst = messages.getOrNull(index - 1)?.from != item.from
            // Is the next message NOT from this user?
            val isLast = messages.getOrNull(index + 1)?.from != item.from

            val offsetPx = remember { mutableStateOf(0f) }
            Row(modifier = Modifier.offsetPx(offsetPx)) {
                if(item.from != me) {
                    val avatarModifier = Modifier
                        .padding(8.dp, 0.dp)
                        .size(24.dp)
                        .align(Alignment.Bottom)

                    if(isLast) {
                        CircleImage(imageData = user.picture.thumbnail, avatarModifier)
                    } else {
                        Spacer(modifier = avatarModifier)
                    }
                }

                MessageItem(
                    item = item,
                    isFirst = isFirst,
                    isLast = isLast,
                    themeColor = themeColor,
                    modifier = Modifier.fillMaxSize()
                        .padding(
                            start = if(item.from == me) 100.dp else 4.dp,
                            end = if(item.from == me) 12.dp else 100.dp,
                            top = if (isFirst) 4.dp else 2.dp,
                            bottom = if (isLast) 4.dp else 2.dp
                        )
                        .wrapContentSize(if(item.from == me) Alignment.CenterEnd else Alignment.CenterStart)
                        .draggableMessage(
                            offsetPx,
                            reverse = item.from == me,
                            onDragStopped = { offset ->
                                if(abs(offset) >= 50) onReplyDragStopped(item)
                            }
                        ) { offset ->
                            offsetPx.value = offset
                        },
                    onEmojiAnimationEnd = onEmojiAnimationEnd,
                    onReactionSelected = { id -> viewModel.addReaction(item, id) }
                )
            }
        }

        item {
            // This acts as a placeholder while the emoji button is pressed
            EmojiMessage(
                id = emojiId,
                tint = if(emojiId == THUMBS_UP) themeColor else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 100.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
                    .wrapContentSize(Alignment.CenterEnd),
                size = transitionState[emojiSize],
                rotation = transitionState[rotation],
                shape = null
            )
        }
    }
}

@Composable
fun MessageItem(
    item: Message,
    isFirst: Boolean,
    isLast: Boolean,
    themeColor: Color,
    modifier: Modifier = Modifier,
    onEmojiAnimationEnd: (emoji: Message.Emoji) -> Unit,
    onReactionSelected: (reactionId: String) -> Unit
) {
    var popupVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = if(item.from == me) Alignment.End else Alignment.Start
    ) {

        item.repliedTo?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Rounded.Reply,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colors.onSurface
                )
                val text = when(item.from) {
                    me -> {
                        if(it.name == me.name.first) "You replied to yourself"
                        else "You replied to ${it.name}"
                    }
                    else -> {
                        if(it.name == me.name.first) "${it.name} replied to you"
                        else "${it.name} replied to themself"
                    }
                }

                Text(text, style = MaterialTheme.typography.caption)
            }
            Spacer(modifier = Modifier.height(6.dp))

            val padding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 32.dp)
            val bgColor = messageBackground().copy(alpha = 0.5f)
            val shape = RoundedCornerShape(18.dp)
            when (it.message) {
                is Message.Emoji -> {
                    EmojiMessage(
                        id = it.message.id,
                        size = 16.dp,
                        tint = if (it.message.id == THUMBS_UP) themeColor else null,
                        paddingValues = padding,
                        backgroundColor = bgColor,
                        shape = shape
                    )
                }
                is Message.Text -> {
                    TextMessage(
                        text = it.message.message,
                        paddingValues = padding,
                        backgroundColor = bgColor,
                        contentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        shape = shape
                    )
                }
            }
        }

        Box(modifier = Modifier.offset(y = if(item.repliedTo != null) -(8).dp else 0.dp)) {
            val messageModifier = Modifier
                .padding(bottom = if (item.reactions.isEmpty()) 0.dp else 18.dp)
                .clickable(indication = null, onLongClick = { popupVisible = true }) {}

            when (item) {
                is Message.Emoji -> {
                    EmojiMessageItem(item, themeColor, messageModifier, onEmojiAnimationEnd)
                }
                is Message.Text -> {
                    TextMessageItem(item, isFirst, isLast, themeColor, messageModifier)
                }
            }

            item.reactions.takeIf { it.isNotEmpty() }?.let { reactions ->
                Reactions(reactions, 12.dp, Modifier.align(Alignment.BottomEnd))
            }

            if (popupVisible) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(0, -150),
                    onDismissRequest = { popupVisible = false }
                ) {
                    ReactionPopup(
                        emojiSize = 28.dp,
                        onReactionSelected = {
                            popupVisible = false
                            onReactionSelected(it)
                        }
                    )
                }
            }
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
    when (item.from) {
        me -> {
            backgroundColor = themeColor
            contentColor = Color.White
        }
        else -> {
            backgroundColor = messageBackground()
            contentColor = contentColorFor(color = backgroundColor)
        }
    }

    TextMessage(
        text = item.message,
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(
            topLeft = if (item.from == me) 18.dp else topVariedCorner,
            topRight = if (item.from == me) topVariedCorner else 18.dp,
            bottomRight = if (item.from == me) bottomVariedCorner else 18.dp,
            bottomLeft = if (item.from == me) 18.dp else bottomVariedCorner
        )
    )
}

@Composable
fun TextMessage(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    paddingValues: PaddingValues = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
    shape: Shape?
) {
    val shapeModifier = if(shape != null) Modifier.clip(shape) else Modifier
    Box(
        modifier = modifier
            .then(shapeModifier)
            .background(backgroundColor)
            .padding(paddingValues)
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
        modifier = modifier,
        shape = null
    )
}

@Composable
fun EmojiMessage(
    id: String,
    size: Dp,
    tint: Color? = null,
    rotation: Float = 0f,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    paddingValues: PaddingValues = PaddingValues(),
    shape: Shape?
) {
    val bgModifier = if(backgroundColor != null) Modifier.background(backgroundColor) else Modifier
    val shapeModifier = if(shape != null) Modifier.clip(shape) else Modifier
    Box(
        modifier = modifier
            .then(shapeModifier)
            .then(bgModifier)
            .padding(paddingValues)
    ) {
        CoilImage(
            data = resIdFor(id) ?: "",
            colorFilter = tint?.let { ColorFilter.tint(it) },
            modifier = modifier
                .size(size)
                .drawLayer(rotationZ = rotation)
        )
    }
}

@Composable
fun Reactions(
    reactions: List<Reaction>,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.surface)
            .padding(2.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurfaceLowEmphasis)
            .padding(8.dp, 4.dp)
    ) {
        reactions.forEach { reaction ->
            CoilImage(
                resIdFor(reaction.id) ?: "",
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
fun ReactionPopup(
    emojiSize: Dp,
    modifier: Modifier = Modifier,
    onReactionSelected: (reactionId: String) -> Unit
) {
    Row(
        modifier = Modifier
            .drawShadow(4.dp, CircleShape)
            .background(MaterialTheme.colors.surface, CircleShape)
            .padding(16.dp, 16.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for(reactionId in listOf(HEART_1, TEARS_OF_JOY, STRAIGHT_FACE, CLAPPING_HANDS, SWEAR_FACE, POO)) {
            CoilImage(
                resIdFor(reactionId) ?: "",
                modifier = Modifier.size(emojiSize).clickable { onReactionSelected(reactionId) },
                fadeIn = true
            )
        }
    }
}

fun Modifier.draggableMessage(
    currentOffset: State<Float>,
    reverse: Boolean,
    deltaScale: Float = 1/4f,
    onDragStopped: (Float) -> Unit,
    onOffsetChange: (offset: Float) -> Unit
) = draggable(
    Orientation.Horizontal,
    onDragStopped = { onDragStopped(currentOffset.value); onOffsetChange(0f);  },
    onDrag = {
        val delta = it * deltaScale
        val update = if(reverse) currentOffset.value + delta <= 0 else currentOffset.value + delta >= 0
        if(update) onOffsetChange(currentOffset.value + delta)
    }
)
