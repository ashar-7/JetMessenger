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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.NamedIcon
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleImage
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis

// TODO: integrate with view model

const val cols = 4
val themeColors = listOf(
    messengerBlue,
    Color(red = 0, green = 132, blue = 255),
    Color(red = 68, green = 191, blue = 199),
    Color(red = 255, green = 195, blue = 0),
    Color(red = 251, green = 60, blue = 76),
    Color(red = 214, green = 50, blue = 187),
    Color(red = 102, green = 154, blue = 204),
    Color(red = 18, green = 207, blue = 19),
    Color(red = 255, green = 126, blue = 42),
    Color(red = 231, green = 133, blue = 134),
    Color(red = 118, green = 70, blue = 254),
    Color(red = 32, green = 205, blue = 245),
    Color(red = 103, green = 184, blue = 105),
    Color(red = 212, green = 168, blue = 141),
    Color(red = 255, green = 92, blue = 161),
    Color(red = 166, green = 150, blue = 199),
).chunked(cols).map { row ->
    // Fill list with Color.Transparent so that size == cols
    row.plus(List(size = cols - row.size) { null })
}

val Audio = NamedIcon("Audio", Icons.Rounded.Call)
val Video = NamedIcon("Video", Icons.Rounded.VideoCall)
val Profile = NamedIcon("Profile", Icons.Rounded.Person)
val Mute = NamedIcon("Mute", Icons.Rounded.Notifications)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Routing.Root.Conversation.Info.Content(
    user: User,
    themeColor: Color,
    onColorSelected: (Color) -> Unit,
    onBackPress: () -> Unit
) {
    var dialogVisible by remember { mutableStateOf(false) }

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
            Surface(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp, 0.dp),
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
                    Row(
                        modifier = Modifier
                            .clickable(onClick = { dialogVisible = true })
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Theme", style = MaterialTheme.typography.subtitle1)

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(themeColor)
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = dialogVisible) {
                Dialog(onDismissRequest = { dialogVisible = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                onClick = { dialogVisible = false },
                                indication = null
                            )
                    ) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                           Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                               Text(text = "Customize your chat", style = MaterialTheme.typography.h6.copy(fontSize = 18.sp))
                               Spacer(modifier = Modifier.height(16.dp).fillMaxWidth())
                               ColorsGrid(themeColors, onSelected = {
                                   onColorSelected(it)
                                   dialogVisible = false
                               })
                           }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconsRow(
    namedIcons : List<NamedIcon>,
    modifier: Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        for(infoIcon in namedIcons) {
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
                Text(text = infoIcon.name, style = MaterialTheme.typography.body1.copy(fontSize = 12.sp))
            }
        }
    }
}

@Composable
fun ColorsGrid(
    colors: List<List<Color?>>,
    circleSize: Dp = 45.dp,
    onSelected: (Color) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        for (row in colors) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for(color in row ) {
                    Box(
                        modifier = Modifier
                            .size(circleSize)
                            .clip(CircleShape)
                            .background(color ?: Color.Transparent)
                            .clickable(enabled = color != null, onClick = { color?.let(onSelected) })
                    )
                }
            }
        }
    }
}
