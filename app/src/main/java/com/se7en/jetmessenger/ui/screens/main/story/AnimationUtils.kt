package com.se7en.jetmessenger.ui.screens.main.story

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.ManualAnimationClock
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import java.util.*
import kotlin.concurrent.timerTask

const val totalStoryTimeMillis = 5000

@ExperimentalAnimationApi
val DefaultStorySlideInVertically = slideInVertically(
    initialOffsetY = { it }, animSpec = tween(500)
)

@ExperimentalAnimationApi
val DefaultStorySlideOutVertically = slideOutVertically(
    targetOffsetY = { it * 2 }, animSpec = tween(500)
)

class StoryAnimationClock(private val totalMillis: Long) {
    private val timer = Timer()
    private lateinit var task: TimerTask

    val clock = ManualAnimationClock(0L)
    var isPaused: Boolean = false

    fun pause() { isPaused = true }
    fun resume() { isPaused = false }

    fun start(onEnd: () -> Unit) {
        // reset clock and cancel the previous task
        isPaused = false
        clock.clockTimeMillis = 0
        if(this::task.isInitialized) task.cancel()

        // schedule new task
        task = timerTask {
            if(!isPaused)
                clock.clockTimeMillis += 1

            if(clock.clockTimeMillis >= totalMillis) {
                onEnd()
            }
        }
        timer.scheduleAtFixedRate(task, 500, 1)
    }

    fun destroy() {
        // cancel timer and the current task
        if(this::task.isInitialized) task.cancel()
        timer.cancel()
    }
}