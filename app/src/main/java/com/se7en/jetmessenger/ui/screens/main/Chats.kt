package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.StoryStatus
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.components.CircleBadgeAvatar
import com.se7en.jetmessenger.ui.components.CircleBorderAvatar
import com.se7en.jetmessenger.ui.components.SearchButton
import com.se7en.jetmessenger.ui.components.UserAvatarWithTitle
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import com.se7en.jetmessenger.ui.theme.typography
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalLazyDsl::class, ExperimentalCoroutinesApi::class)
@Composable
fun Routing.Main.BottomNav.Chats.Content(
    users: List<User>,
    onChatClick: (user: User) -> Unit,
    onSearchClick: () -> Unit
) {
    LazyColumn {
        item {
            SearchButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.onSurfaceLowEmphasis,
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

        UserAvatarWithTitle(
            title = user.name.first,
            imageData = user.picture.medium,
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

    Row(
        modifier = modifier
            .padding(12.dp, 8.dp)
            .fillMaxWidth()
    ) {
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
            ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high) {
                Text(
                    "${user.name.first} ${user.name.last}",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            ProvideTextStyle(value = typography.caption.copy(
                fontSize = 14.sp
            )) {
                ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
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
