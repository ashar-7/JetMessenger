package com.se7en.jetmessenger.ui.theme

import android.content.Context
import android.util.LruCache
import androidx.compose.material.Colors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val messengerBlue = Color(0xFF0078FF)
val messengerLightBlue = Color(0xFF00C6FF)

val Colors.onSurfaceLowEmphasis: Color
    get() = onSurface.copy(alpha = 0.12f)

// DominantColorState copied and modified from Jetcaster:
// https://github.com/android/compose-samples/blob/master/Jetcaster/app/src/main/java/com/example/jetcaster/util/DynamicTheming.kt

@Composable
fun rememberDominantColorState(
    context: Context = ContextAmbient.current,
    defaultColor: Color = Color.LightGray,
    cacheSize: Int = 12
): DominantColorState = remember {
    DominantColorState(context, defaultColor, cacheSize)
}

@Stable
class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
    cacheSize: Int = 12,
) {
    var color by mutableStateOf(defaultColor)
        private set

    private val cache = when {
        cacheSize > 0 -> LruCache<String, Color>(cacheSize)
        else -> null
    }

    suspend fun updateColorsFromImageUrl(url: String) {
        val resultColor = calculateDominantColor(url)
        color = resultColor ?: defaultColor
    }

    private suspend fun calculateDominantColor(url: String): Color? {
        val cached = cache?.get(url)
        if (cached != null) {
            // If we already have the result cached, return early now...
            return cached
        }

        // Otherwise we calculate the swatches in the image, and return the first valid color
        return calculateSwatchesInImage(context, url)
            // get the color with max population
            .maxByOrNull { swatch -> swatch.population }
            ?.let { swatch ->
                Color(swatch.rgb)
            }
            // Cache the resulting [DominantColors]
            ?.also { result -> cache?.put(url, result) }
    }

    /**
     * Reset the color values to [defaultColor].
     */
    fun reset() {
        color = defaultColor
    }
}

/**
 * Fetches the given [imageUrl] with [Coil], then uses [Palette] to calculate the dominant color.
 */
private suspend fun calculateSwatchesInImage(
    context: Context,
    imageUrl: String
): List<Palette.Swatch> {
    val r = ImageRequest.Builder(context)
        .data(imageUrl)
        // We scale the image to cover 128px x 128px (i.e. min dimension == 128px)
        .size(128).scale(Scale.FILL)
        // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
        .allowHardware(false)
        .build()

    val bitmap = when (val result = Coil.execute(r)) {
        is SuccessResult -> result.drawable.toBitmap()
        else -> null
    }

    return bitmap?.let {
        withContext(Dispatchers.Default) {
            val palette = Palette.Builder(bitmap)
                // Disable any bitmap resizing in Palette. We've already loaded an appropriately
                // sized bitmap through Coil
                .resizeBitmapArea(0)
                // Clear any built-in filters. We want the unfiltered dominant color
                .clearFilters()
                // We reduce the maximum color count down to 8
                .maximumColorCount(8)
                .generate()

            palette.swatches
        }
    } ?: emptyList()
}