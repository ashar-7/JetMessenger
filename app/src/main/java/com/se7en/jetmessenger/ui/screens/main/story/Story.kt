package com.se7en.jetmessenger.ui.screens.main.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedTask
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.se7en.jetmessenger.data.models.Story
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.theme.rememberDominantColorState
import com.se7en.jetmessenger.ui.theme.verticalGradient
import dev.chrisbanes.accompanist.coil.CoilImage


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Routing.Root.Main.Story.Content(
    story: Story?,
    onClose: () -> Unit
) {
    val colorState = rememberDominantColorState()

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it }, animSpec = tween(500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it * 2 }, animSpec = tween(500)
        )
    ) {
        story?.let { story ->
            LaunchedTask(story.thumbnailUrl){
                colorState.updateColorsFromImageUrl(story.thumbnailUrl)
            }

            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalGradient(colors = listOf(colorState.color, colorState.color.copy(alpha = 0.5f)))
                        .clickable(onClick = {})
                ) {
                    CoilImage(
                        data = story.imageUrl,
                        modifier = Modifier.align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )

                    TopBar(
                        story,
                        modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
                        onMore = {},
                        onClose = onClose
                    )
                }
            }
        }
    }
}