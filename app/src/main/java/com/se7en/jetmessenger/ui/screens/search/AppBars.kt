package com.se7en.jetmessenger.ui.screens.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.se7en.jetmessenger.ui.Routing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Routing.Root.Search.TopBar(
    onBackPress: () -> Unit,
    onSearch: (query: String) -> Unit,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    var query: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    TopAppBar(
        title = {
            Box {
                BaseTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        onSearch(query.text)
                    },
                    modifier = Modifier.align(Alignment.CenterStart).padding(0.dp, 8.dp),
                    textStyle = MaterialTheme.typography.subtitle1,
                    cursorColor = MaterialTheme.colors.primary,
                    textColor = MaterialTheme.colors.onSurface,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send,
                    onImeActionPerformed = { onSearch(query.text) }
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
