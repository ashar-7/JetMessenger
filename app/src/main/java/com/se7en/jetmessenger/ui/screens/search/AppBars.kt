package com.se7en.jetmessenger.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.se7en.jetmessenger.ui.screens.Routing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Routing.Search.TopBar(
    onBackPress: () -> Unit,
    onSearch: (query: String) -> Unit,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    var query: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    TopAppBar(
        title = {
            Box {
                BasicTextField(
                    value = query,
                    onValueChange = { query = it; onSearch(query.text) },
                    modifier = Modifier.align(Alignment.CenterStart).padding(0.dp, 8.dp),
                    textStyle = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onSurface),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Search
                    ),
                    onImeActionPerformed = { onSearch(query.text) },
                    visualTransformation = VisualTransformation.None,
                    cursorColor = MaterialTheme.colors.primary
                )
                if (query.text.isEmpty()) {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.align(Alignment.CenterStart),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(Icons.Rounded.ArrowBack)
            }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = if (isSystemInDarkTheme()) 0.dp else 4.dp
    )
}
