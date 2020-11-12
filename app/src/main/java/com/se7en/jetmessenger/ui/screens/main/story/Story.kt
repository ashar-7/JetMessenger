package com.se7en.jetmessenger.ui.screens.main.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.layout.ContentScale
import com.se7en.jetmessenger.data.models.Story
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.ui.theme.rememberDominantColorState
import com.se7en.jetmessenger.ui.theme.verticalGradient
import dev.chrisbanes.accompanist.coil.CoilImage

// TODO: Next and previous story navigation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Routing.Main.Story.Content(
    visible: Boolean,
    story: Story?,
    onStorySeen: (Story) -> Unit,
    onNext: (Story) -> Unit,
    onClose: () -> Unit
) {
    val colorState = rememberDominantColorState()

    AnimatedVisibility(
        visible = visible,
        enter = DefaultStorySlideInVertically,
        exit = DefaultStorySlideOutVertically
    ) {
        story?.let { story ->
            val animationClock by remember {
                mutableStateOf(StoryAnimationClock(totalStoryTimeMillis.toLong()))
            }
            val progress = animatedFloat(initVal = 0f, clock = animationClock.clock)

            LaunchedEffect(story.thumbnailUrl) {
                colorState.updateColorsFromImageUrl(story.thumbnailUrl)
            }

            onCommit(story) {
                animationClock.start(onEnd = { onNext(story) })
                progress.snapTo(targetValue = 0f)
                progress.animateTo(targetValue = 1f, tween(totalStoryTimeMillis, easing = LinearEasing))
                onStorySeen(story)
            }
            onDispose { animationClock.destroy() }

            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalGradient(colors = listOf(colorState.color, colorState.color.copy(alpha = 0.5f)))
                        .pressIndicatorGestureFilter(
                            onStart = { animationClock.pause() },
                            onStop = { animationClock.resume() },
                            onCancel = { animationClock.resume() }
                        )
                ) {
                    CoilImage(
                        data = story.imageUrl,
                        modifier = Modifier.align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )

                    TopBar(
                        story,
                        progress = progress.value,
                        visible = !animationClock.isPaused,
                        modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
                        onMore = {},
                        onClose = onClose
                    )
                }
            }
        }
    }
}