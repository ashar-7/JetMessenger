package com.se7en.jetmessenger.ui.screens.conversation.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VideoCall
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.NamedIcon
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleImage
import com.se7en.jetmessenger.ui.screens.conversation.*
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import dev.chrisbanes.accompanist.coil.CoilImage

// TODO: integrate with view model

val Audio = NamedIcon("Audio", Icons.Rounded.Call)
val Video = NamedIcon("Video", Icons.Rounded.VideoCall)
val Profile = NamedIcon("Profile", Icons.Rounded.Person)
val Mute = NamedIcon("Mute", Icons.Rounded.Notifications)

// Grid columns
private const val cols = 4

val themeColorsGridList = themeColors.toGridList(cols)
val chatEmojiIdsGridList = listOf(
    THUMBS_UP,
    POO
).toGridList(cols)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Routing.Root.Conversation.Info.Content(
    user: User,
    themeColor: Color,
    currentEmojiId: String,
    onColorSelected: (Color) -> Unit,
    onEmojiSelected: (String) -> Unit,
    onBackPress: () -> Unit
) {
    var themeDialogVisible by remember { mutableStateOf(false) }
    var emojiDialogVisible by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { it }, animSpec = tween(300)),
        exit = slideOutHorizontally(targetOffsetX = { it }, animSpec = tween(300))
    ) {
        Scaffold(
            topBar = {
                TopBar(onBackPress = onBackPress, onMoreClick = {})
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleImage(
                    imageData = user.picture.large,
                    modifier = Modifier.size(120.dp, 120.dp).padding(8.dp, 8.dp)
                )
                Text(
                    text = "${user.name.first} ${user.name.last}",
                    style = MaterialTheme.typography.h6
                )
                IconsRow(
                    listOf(Audio, Video, Profile, Mute),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                ThemeSelectorButton(
                    currentThemeColor = themeColor,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    onClick = { themeDialogVisible = true }
                )
                EmojiSelectorButton(
                    currentEmojiId = currentEmojiId,
                    themeColor = themeColor,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    onClick = { emojiDialogVisible = true }
                )
            }

            if(themeDialogVisible) {
                ContentDialog(
                    title = "Customize your chat",
                    onDismissRequest = { themeDialogVisible = false }
                ) {
                    Grid(
                        themeColorsGridList,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    ) { color ->
                        ColorGridItem(
                            color = color,
                            onSelected = {
                                onColorSelected(it)
                                themeDialogVisible = false
                            }
                        )
                    }
                }
            }

            if(emojiDialogVisible) {
                ContentDialog(
                    title = "Customize your chat",
                    onDismissRequest = { emojiDialogVisible = false }
                ) {
                    Grid(
                        chatEmojiIdsGridList,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    ) { id ->
                        EmojiGridItem(
                            id = id,
                            tint = if(id == THUMBS_UP) themeColor else null,
                            onSelected = {
                                onEmojiSelected(it)
                                emojiDialogVisible = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IconsRow(
    namedIcons: List<NamedIcon>,
    modifier: Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        for (infoIcon in namedIcons) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.onSurfaceLowEmphasis)
                        .padding(4.dp)
                        .size(28.dp)
                ) {
                    Icon(asset = infoIcon.icon)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = infoIcon.name,
                    style = MaterialTheme.typography.body1.copy(fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
fun ThemeSelectorButton(
    currentThemeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Theme", style = MaterialTheme.typography.subtitle1)

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(currentThemeColor)
                .padding(8.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )
    }
}

@Composable
fun EmojiSelectorButton(
    currentEmojiId: String,
    themeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Emoji", style = MaterialTheme.typography.subtitle1)

        CoilImage(
            data = resIdFor(currentEmojiId) ?: "",
            colorFilter = if(currentEmojiId == THUMBS_UP) ColorFilter.tint(themeColor) else null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ContentDialog(
    title: String,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = onDismissRequest,
                    indication = null
                )
        ) {
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth())

                    content()
                }
            }
        }
    }
}

@Composable
fun <T> Grid(
    list: List<List<T?>>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T?) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        for (row in list) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (item in row) {
                    itemContent(item)
                }
            }
        }
    }
}

@Composable
fun ColorGridItem(
    color: Color?,
    size: Dp = 45.dp,
    shape: Shape = CircleShape,
    onSelected: (Color) -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(color ?: Color.Transparent)
            .clickable(
                enabled = color != null,
                onClick = { color?.let(onSelected) }
            )
    )
}

@Composable
fun EmojiGridItem(
    id: String?,
    tint: Color? = null,
    size: Dp = 45.dp,
    onSelected: (String) -> Unit
) {
    CoilImage(
        data = resIdFor(id) ?: "",
        colorFilter = tint?.let { ColorFilter.tint(it) },
        modifier = Modifier
            .size(size)
            .clickable(
                enabled = id != null,
                onClick = { id?.let(onSelected) }
            )
    )
}
