package com.se7en.jetmessenger.ui.screens.conversation

import androidx.compose.ui.graphics.Color
import com.se7en.jetmessenger.R
import com.se7en.jetmessenger.ui.theme.messengerBlue

const val ALIEN = "alien"
const val BICEPS_FLEX = "biceps_flex"
const val BLOWING_KISS = "blowing_kiss"
const val CLAPPING_HANDS = "clapping_hands"
const val EAGLE = "eagle"
const val EYES = "eyes"
const val FIRE = "fire"
const val FIST = "fist"
const val HEART_1 = "heart_1"
const val HEART_EYES = "heart_eyes"
const val MONKEY_FACE = "monkey_face"
const val POO = "poo"
const val RED_APPLE = "red_apple"
const val SKULL = "skull"
const val STRAIGHT_FACE = "straight_face"
const val SWEAR_FACE = "swear_face"
const val THUMBS_UP = "thumbs_up"
const val TEARS_OF_JOY = "tears_of_joy"

val themeColors = listOf(
    messengerBlue,
    Color(red = 0, green = 132, blue = 255),
    Color(red = 68, green = 191, blue = 199),
    Color(red = 255, green = 195, blue = 0),
    Color(red = 251, green = 60, blue = 76),
    Color(red = 214, green = 50, blue = 187),
    Color(red = 102, green = 154, blue = 204),
    Color(red = 18, green = 207, blue = 19),
    Color(red = 255, green = 126, blue = 42),
    Color(red = 231, green = 133, blue = 134),
    Color(red = 118, green = 70, blue = 254),
    Color(red = 32, green = 205, blue = 245),
    Color(red = 103, green = 184, blue = 105),
    Color(red = 212, green = 168, blue = 141),
    Color(red = 255, green = 92, blue = 161),
    Color(red = 166, green = 150, blue = 199),
)

val chatEmojiIds = listOf(
    THUMBS_UP, ALIEN, BICEPS_FLEX, BLOWING_KISS, CLAPPING_HANDS, EAGLE, EYES, FIRE, FIST, HEART_1, HEART_EYES,
    MONKEY_FACE, POO, RED_APPLE, SKULL, STRAIGHT_FACE, SWEAR_FACE, TEARS_OF_JOY
)

fun resIdFor(emojiId: String?): Int? {
    return when(emojiId) {
        ALIEN -> R.drawable.alien
        BICEPS_FLEX -> R.drawable.biceps_flex
        BLOWING_KISS -> R.drawable.blowing_kiss
        CLAPPING_HANDS -> R.drawable.clapping_hands
        EAGLE -> R.drawable.eagle
        EYES -> R.drawable.eyes
        FIRE -> R.drawable.fire
        FIST -> R.drawable.fist
        HEART_1 -> R.drawable.heart_1
        HEART_EYES -> R.drawable.heart_eyes
        MONKEY_FACE -> R.drawable.monkey_face
        POO -> R.drawable.poo
        RED_APPLE -> R.drawable.red_apple
        SKULL -> R.drawable.skull
        STRAIGHT_FACE -> R.drawable.straight_face
        SWEAR_FACE -> R.drawable.swear_face
        THUMBS_UP -> R.drawable.thumbs_up
        TEARS_OF_JOY -> R.drawable.tears_of_joy
        else -> null
    }
}

fun <T> List<T>.toGridList(cols: Int): List<List<T?>> =
    chunked(cols).map { row ->
        // Fill list with null values so that size == cols
        row.plus(List(size = cols - row.size) { null })
    }
