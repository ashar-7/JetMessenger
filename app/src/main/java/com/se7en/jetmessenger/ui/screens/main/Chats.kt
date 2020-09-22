package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.StoryStatus
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleBadgeAvatar
import com.se7en.jetmessenger.ui.components.CircleBorderAvatar
import com.se7en.jetmessenger.ui.components.SearchButton
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import com.se7en.jetmessenger.ui.theme.typography
import com.se7en.jetmessenger.viewmodels.MainViewModel


@OptIn(ExperimentalLazyDsl::class)
@Composable
fun Routing.BottomNav.Chats.Content(
    onChatClick: (user: User) -> Unit,
    onSearchClick: () -> Unit
) {
    val viewModel: MainViewModel = viewModel()
    val users: List<User> by viewModel.users.observeAsState(listOf())

    LazyColumn {
        item {
            SearchButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.onSurfaceLowEmphasis(),
                contentColor = MaterialTheme.colors.onSurface.copy(
                    alpha = 0.5f
                )
            )
        }
        item {
            ActiveFriendsRow(
                users.filterIndexed { index, _ -> index % 3 == 0 },
                onChatClick
            )
        }
        itemsIndexed(users) { index, user ->
            ChatItem(
                user = user,
                lastMessage = "Great! How are you doing?",
                dateTime = "22:35",
                storyStatus = when {
                    index % 3 == 0 -> StoryStatus.AVAILABLE_SEEN
                    index % 2 == 0 -> StoryStatus.AVAILABLE_NOT_SEEN
                    else -> StoryStatus.UNAVAILABLE
                },
                isOnline = index % 3 == 0,
                modifier = Modifier.clickable(onClick = { onChatClick(user) })
            )
        }
    }
}

@Composable
fun ActiveFriendsRow(
    users: List<User>,
    onItemClick: (user: User) -> Unit
) {
    LazyRowForIndexed(
        items = users,
        contentPadding = PaddingValues(2.dp)
    ) { index, user ->
        val padding = 4.dp
        val (startPadding, endPadding) = when(index) {
            0 -> Pair(8.dp, padding)
            users.lastIndex -> Pair(padding, 8.dp)
            else -> Pair(padding, padding)
        }

        ActiveFriendAvatarItem(
            user = user,
            imageSize = 56.dp,
            modifier = Modifier
                .clickable(onClick = { onItemClick(user) })
                .padding(
                    start = startPadding,
                    end = endPadding,
                    top = padding,
                    bottom = padding
                )
        )
    }
}

@Composable
fun ChatItem(
    user: User,
    lastMessage: String,
    dateTime: String,
    storyStatus: StoryStatus = StoryStatus.UNAVAILABLE,
    isOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    val avatarBorder = when(storyStatus) {
        StoryStatus.UNAVAILABLE -> null
        StoryStatus.AVAILABLE_SEEN ->
            BorderStroke(2.dp, Color.LightGray)
        StoryStatus.AVAILABLE_NOT_SEEN ->
            BorderStroke(2.dp, messengerBlue)
    }

    Box(
        modifier = modifier
            .padding(12.dp, 8.dp)
            .fillMaxWidth()
    ) {
        Row {
            when {
                isOnline -> {
                    CircleBadgeAvatar(
                        imageData = user.picture.medium,
                        size = 56.dp
                    )
                }
                else -> CircleBorderAvatar(
                    imageData = user.picture.medium,
                    size = 56.dp,
                    border = avatarBorder
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
                    Text(
                        "${user.name.first} ${user.name.last}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Spacer(modifier = Modifier.padding(2.dp))

                ProvideTextStyle(value = typography.caption.copy(
                    fontSize = 14.sp
                )) {
                    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                        LastMessageWithDateTime(
                            user.name.first,
                            lastMessage,
                            dateTime
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveFriendAvatarItem(
    user: User,
    imageSize: Dp = 56.dp,
    modifier: Modifier = Modifier.padding(4.dp)
) {
    ConstraintLayout(modifier) {
        val (avatar, name) = createRefs()

        val avatarModifier = Modifier.constrainAs(avatar) {
            top.linkTo(parent.top, margin = 2.dp)
            linkTo(
                start = parent.start,
                end = parent.end,
                startMargin = 2.dp,
                endMargin = 2.dp
            )
            width = Dimension.preferredWrapContent
        }

        val nameModifier = Modifier.constrainAs(name) {
            linkTo(
                top = avatar.bottom,
                bottom = parent.bottom,
                start = parent.start,
                end = parent.end,
                startMargin = 2.dp,
                endMargin = 2.dp,
                topMargin = 2.dp,
                bottomMargin = 2.dp,
            )
            width = Dimension.fillToConstraints
        }

        CircleBadgeAvatar(
            imageData = user.picture.medium,
            size = imageSize,
            modifier = avatarModifier
        )

        ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
            Text(
                text = user.name.first,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = nameModifier
            )
        }
    }
}

@Composable
fun LastMessageWithDateTime(
    name: String,
    lastMessage: String,
    dateTime: String
) {
    Row {
        Text(
            "$name: $lastMessage",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, false)
        )
        Text(" • $dateTime", maxLines = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun ChatItem() {
    ChatItem(
        me,
        "Hey there",
        "17:45",
    )
}

@Preview(showBackground = true)
@Composable
fun ActiveFriendAvatarItem() {
    ActiveFriendAvatarItem(me)
}
