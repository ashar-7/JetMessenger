package com.se7en.jetmessenger.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.*

// Source: https://github.com/android/compose-samples/blob/main/Jetchat/app/src/main/java/com/example/compose/jetchat/conversation/BackHandler.kt

/**
 * This [Composable] can be used with a [AmbientBackPressedDispatcher] to intercept a back press (if
 * [enabled]).
 */
@Composable
fun backPressHandler(onBackPressed: () -> Unit, enabled: Boolean = true) {
    val dispatcher = AmbientBackPressedDispatcher.current.onBackPressedDispatcher

    // This callback is going to be remembered only if onBackPressed is referentially equal.
    val backCallback = remember(onBackPressed) {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    // Using onCommit guarantees that failed transactions don't incorrectly toggle the
    // remembered callback.
    onCommit(enabled) {
        backCallback.isEnabled = enabled
    }

    onCommit(dispatcher, onBackPressed) {
        // Whenever there's a new dispatcher set up the callback
        dispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

/**
 * This [Ambient] is used to provide an [OnBackPressedDispatcherOwner]:
 *
 * ```
 * Providers(BackPressedDispatcherAmbient provides requireActivity()) { }
 * ```
 *
 * and setting up the callbacks with [backPressHandler].
 */
val AmbientBackPressedDispatcher =
    staticAmbientOf<OnBackPressedDispatcherOwner> { error("Ambient used without Provider") }