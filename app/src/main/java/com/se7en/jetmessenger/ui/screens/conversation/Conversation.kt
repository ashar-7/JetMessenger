package com.se7en.jetmessenger.ui.screens.conversation

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumnFor(
                items = messages,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp),
            ) { item ->

                ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
                    when(item.from) {
                        me -> {
                            MessageContent(
                                text = item.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 28.dp, end = 12.dp, top = 2.dp, bottom = 2.dp)
                                    .wrapContentSize(Alignment.CenterEnd)
                            )
                        }
                        else -> {
                            MessageContent(
                                text = item.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 28.dp, top = 2.dp, bottom = 2.dp)
                                    .wrapContentSize(Alignment.CenterStart),
                                backgroundColor = MaterialTheme.colors.onSurfaceLowEmphasis()
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
    contentColor: Color = contentColorFor(backgroundColor)
) {
    Box(
        modifier = modifier,
        paddingStart = 12.dp,
        paddingEnd = 12.dp,
        paddingTop = 8.dp,
        paddingBottom = 8.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(18.dp),
        gravity = ContentGravity.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(
                color = contentColor,
                fontSize = 15.sp
            )
        )
    }
}
