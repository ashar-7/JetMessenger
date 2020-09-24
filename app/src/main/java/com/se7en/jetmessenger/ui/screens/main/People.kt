package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.VerticalGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleBadgeAvatar
import com.se7en.jetmessenger.ui.components.CircleBorderAvatar
import com.se7en.jetmessenger.ui.components.SearchButton
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import com.se7en.jetmessenger.viewmodels.UsersViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

@OptIn(ExperimentalLazyDsl::class)
@Composable
fun Routing.BottomNav.People.Content(
    onChatClick: (user: User) -> Unit,
    onSearchClick: () -> Unit
) {
    val viewModel: UsersViewModel = viewModel()
    val users: List<User> by viewModel.users.collectAsState(listOf())

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
        item { StoriesRow(users) }
        item {
            ProvideEmphasis(emphasis = EmphasisAmbient.current.disabled) {
                Text(
                    text = "ACTIVE",
                    modifier = Modifier.padding(14.dp, 8.dp),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 13.sp
                    )
                )
            }
        }
        items(
            users.filterIndexed { index, _ -> index % 3 == 0 }
        ) { user ->
            ActiveFriendItem(
                user = user,
                modifier = Modifier.clickable(onClick = {
                    onChatClick(user)
                })
            )
        }
    }
}

@Composable
fun ActiveFriendItem(
    user: User,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleBadgeAvatar(
                imageData = user.picture.medium,
                size = 40.dp
            )

            Spacer(modifier = Modifier.padding(4.dp))

            ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
                Text(
                    "${user.name.first} ${user.name.last}",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
fun StoriesRow(
    users: List<User>
) {
    LazyRowForIndexed(items = users) { index, user ->
        val padding = when(index) {
            0 -> PaddingValues(8.dp, 2.dp, 2.dp, 2.dp)
            users.lastIndex -> PaddingValues(2.dp, 2.dp, 8.dp, 2.dp)
            else -> PaddingValues(2.dp, 2.dp, 2.dp, 2.dp)
        }

        StoryItem(
            user = user,
            storyThumbnail = "https://picsum.photos/id/${index+10}/200/300",
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun StoryItem(
    user: User,
    storyThumbnail: Any,
    width: Dp = 96.dp,
    height: Dp = 140.dp,
    modifier: Modifier = Modifier
) {
    Card(
        backgroundColor = EmphasisAmbient.current.disabled.applyEmphasis(
            MaterialTheme.colors.surface
        ),
        contentColor = Color.White,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier.size(width, height)
    ) {
        Stack {
            CoilImageWithCrossfade(
                storyThumbnail,
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        VerticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f)
                            ),
                            startY = 0f,
                            endY = height.value
                        )
                    )
            )

            CircleBorderAvatar(
                imageData = user.picture.thumbnail,
                size = 30.dp,
                border = BorderStroke(2.dp, messengerBlue),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
            )

            Text(
                text = "${user.name.first} ${user.name.last}",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp, 4.dp, 4.dp, 4.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
fun ActiveFriendItemPreview() {
    ActiveFriendItem(me)
}

@Preview
@Composable
fun StoryItemPreview() {
    StoryItem(
        user = me,
        storyThumbnail = ""
    )
}
