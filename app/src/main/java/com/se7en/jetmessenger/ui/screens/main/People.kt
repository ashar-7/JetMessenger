package com.se7en.jetmessenger.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
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
import androidx.ui.tooling.preview.Preview
import com.se7en.jetmessenger.data.me
import com.se7en.jetmessenger.data.models.Story
import com.se7en.jetmessenger.data.models.StoryStatus
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleBadgeAvatar
import com.se7en.jetmessenger.ui.components.CircleBorderAvatar
import com.se7en.jetmessenger.ui.components.SearchButton
import com.se7en.jetmessenger.ui.theme.messengerBlue
import com.se7en.jetmessenger.ui.theme.onSurfaceLowEmphasis
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalLazyDsl::class, ExperimentalCoroutinesApi::class)
@Composable
fun Routing.BottomNav.People.Content(
    users: List<User>,
    stories: List<Story>,
    onChatClick: (user: User) -> Unit,
    onSearchClick: () -> Unit,
    onStoryClick: (story: Story) -> Unit
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
        item { StoriesRow(stories, onStoryClick) }
        item {
            ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled) {
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
    Row(
        modifier = modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleBadgeAvatar(
            imageData = user.picture.medium,
            size = 40.dp
        )

        Spacer(modifier = Modifier.padding(4.dp))

        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high) {
            Text(
                "${user.name.first} ${user.name.last}",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@Composable
fun StoriesRow(
    stories: List<Story>,
    onStoryClick: (story: Story) -> Unit
) {
    LazyRowForIndexed(items = stories) { index, story ->
        val padding = when(index) {
            0 -> PaddingValues(8.dp, 2.dp, 2.dp, 2.dp)
            stories.lastIndex -> PaddingValues(2.dp, 2.dp, 8.dp, 2.dp)
            else -> PaddingValues(2.dp, 2.dp, 2.dp, 2.dp)
        }

        StoryItem(
            story = story,
            modifier = Modifier.padding(padding),
            onStoryClick = onStoryClick
        )
    }
}

@Composable
fun StoryItem(
    story: Story,
    width: Dp = 96.dp,
    height: Dp = 140.dp,
    modifier: Modifier = Modifier,
    onStoryClick: (story: Story) -> Unit = {}
) {
    val avatarBorder = when(story.status) {
        StoryStatus.AVAILABLE_NOT_SEEN ->
            BorderStroke(2.dp, messengerBlue)
        else ->
            BorderStroke(2.dp, Color.LightGray)
    }

    Card(
        backgroundColor = AmbientEmphasisLevels.current.disabled.applyEmphasis(
            MaterialTheme.colors.surface
        ),
        contentColor = Color.White,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .size(width, height)
            .clickable(onClick = { onStoryClick(story) })
    ) {
        Box {
            CoilImage(
                story.thumbnailUrl,
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop,
                fadeIn = true
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
                imageData = story.user.picture.thumbnail,
                size = 30.dp,
                border = avatarBorder,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
            )

            Text(
                text = "${story.user.name.first} ${story.user.name.last}",
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
        story = Story(
            me,
            imageUrl = "",
            thumbnailUrl = "",
            status = StoryStatus.AVAILABLE_NOT_SEEN,
            time = "4h"
        )
    )
}
