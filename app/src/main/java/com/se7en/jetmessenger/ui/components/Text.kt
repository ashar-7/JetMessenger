package com.se7en.jetmessenger.ui.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Emphasis
import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SubHeading(
    text: String,
    modifier: Modifier = Modifier.padding(14.dp, 8.dp),
    textStyle: TextStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 13.sp),
    emphasis: Emphasis = EmphasisAmbient.current.disabled
) {
    ProvideEmphasis(emphasis) {
        Text(
            text = text,
            modifier = modifier,
            style = textStyle
        )
    }
}
