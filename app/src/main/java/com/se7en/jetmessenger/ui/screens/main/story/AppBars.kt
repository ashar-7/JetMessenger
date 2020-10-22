package com.se7en.jetmessenger.ui.screens.main.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.se7en.jetmessenger.data.models.Story
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleImage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Routing.Root.Main.Story.TopBar(
    story: Story,
    progress: Float,
    visible: Boolean,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onMore: () -> Unit,
    onClose: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(0f, tween(200, delayMillis = 0, easing = LinearEasing)),
        exit = fadeOut(0f, tween(200, delayMillis = 200, easing = LinearEasing))
    ) {
        Column(
            modifier = modifier.padding(16.dp, 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .height(1.dp)
                    .fillMaxWidth(),
                color = contentColor,
                backgroundColor = Color.Gray
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircleImage(imageData = story.user.picture.medium, modifier = Modifier.size(36.dp))

                    Column {
                        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high) {
                            Text(
                                text = "${story.user.name.first} ${story.user.name.last}",
                                style = MaterialTheme.typography.subtitle2,
                                color = contentColor
                            )
                        }
                        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                            Text(
                                text = story.time,
                                style = MaterialTheme.typography.subtitle2,
                                color = contentColor
                            )
                        }
                    }
                }

                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        Icons.Default.MoreVert,
                        tint = contentColor,
                        modifier = Modifier.clickable(onClick = onMore)
                    )
                    Icon(
                        Icons.Default.Close,
                        tint = contentColor,
                        modifier = Modifier.clickable(onClick = onClose)
                    )
                }
            }
        }
    }
}
