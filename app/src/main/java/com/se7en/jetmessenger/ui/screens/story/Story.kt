package com.se7en.jetmessenger.ui.screens.story

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
import androidx.compose.runtime.launchInComposition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.theme.rememberDominantColorState
import com.se7en.jetmessenger.ui.theme.verticalGradient
import dev.chrisbanes.accompanist.coil.CoilImage


// TODO: integrate with view model (People screen too)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Routing.Root.Main.Story.Content(
    user: User?,
    url: String,
    onClose: () -> Unit
) {
    val colorState = rememberDominantColorState()
    launchInComposition(url) {
        colorState.updateColorsFromImageUrl(url)
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it }, animSpec = tween(500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it * 2 }, animSpec = tween(500)
        )
    ) {
        user?.let {
            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalGradient(colors = listOf(colorState.color, colorState.color.copy(alpha = 0.5f)))
                        .clickable(onClick = {})
                ) {
                    CoilImage(
                        data = url,
                        modifier = Modifier.align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )

                    TopBar(
                        it,
                        modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
                        onMore = {},
                        onClose = onClose
                    )
                }
            }
        }
    }
}