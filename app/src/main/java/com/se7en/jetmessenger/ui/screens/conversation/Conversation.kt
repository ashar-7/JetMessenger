package com.se7en.jetmessenger.ui.screens.conversation

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import com.se7en.jetmessenger.viewmodels.ConversationViewModel

@Composable
fun Routing.Root.Conversation.Content(onBackPress: () -> Unit) {

    val viewModel: ConversationViewModel = viewModel()
    val messages = viewModel.messages.getValue(user)

    Scaffold(
        topBar = {
            TopBar(
                onActionClick = {},
                onBackPress = onBackPress
            )
        },
        bottomBar = {
            BottomBar(
                onSendClick = { message ->
                    viewModel.sendMessage(user, message)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumnForIndexed(
                items = messages,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp),
            ) { index, item ->

                ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
                    // Was the previous message NOT from this user?
                    val isFirst = messages.getOrNull(index-1)?.from != item.from
                    // Is the next message NOT from this user?
                    val isLast = messages.getOrNull(index+1)?.from != item.from

                    when(item.from) {
                        me -> {
                            MessageContent(
                                text = item.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 100.dp,
                                        end = 12.dp,
                                        top = if(isFirst) 4.dp else 1.dp,
                                        bottom = if(isLast) 4.dp else 1.dp
                                    )
                                    .wrapContentSize(Alignment.CenterEnd),
                                topLeftCorner = 18.dp,
                                topRightCorner = if(isFirst) 18.dp else 3.dp,
                                bottomRightCorner = if(isLast) 18.dp else 3.dp,
                                bottomLeftCorner = 18.dp
                            )
                        }
                        else -> {
                            MessageContent(
                                text = item.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 12.dp,
                                        end = 100.dp,
                                        top = if(isFirst) 4.dp else 2.dp,
                                        bottom = if(isLast) 4.dp else 2.dp
                                    )
                                    .wrapContentSize(Alignment.CenterStart),
                                backgroundColor = MaterialTheme.colors.onSurfaceLowEmphasis(),
                                topLeftCorner = if(isFirst) 18.dp else 3.dp,
                                topRightCorner = 18.dp,
                                bottomRightCorner = 18.dp,
                                bottomLeftCorner = if(isLast) 18.dp else 3.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageContent(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    topLeftCorner: Dp = 0.dp,
    topRightCorner: Dp = 0.dp,
    bottomRightCorner: Dp = 0.dp,
    bottomLeftCorner: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topLeftCorner, topRightCorner, bottomRightCorner, bottomLeftCorner))
            .background(backgroundColor)
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
