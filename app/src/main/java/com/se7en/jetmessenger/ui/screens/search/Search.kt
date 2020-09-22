package com.se7en.jetmessenger.ui.screens.search

import androidx.compose.foundation.Box
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.Routing
import com.se7en.jetmessenger.ui.components.CircleBadgeAvatar
import com.se7en.jetmessenger.viewmodels.MainViewModel

// TODO: No results, recent searches, suggested
@Composable
fun Routing.Root.Search.Content(
    onBackPress: () -> Unit,
    onUserClick: (user: User) -> Unit
) {
    val viewModel: MainViewModel = viewModel()
    val (query, setQuery) = remember { mutableStateOf("") }
    val results: List<User> by viewModel.searchUsers(query).collectAsState(listOf())

    Scaffold(
        topBar = {
            TopBar(onBackPress = onBackPress, onSearch = setQuery)
        }
    ) { innerPadding ->
        ScrollableColumn(modifier = Modifier.padding(innerPadding)) {
            results.forEachIndexed { index, user ->
                UserResultItem(
                    user = user,
                    isOnline = index % 3 == 0,
                    modifier = Modifier.clickable(onClick = { onUserClick(user) }),
                )
            }
        }
    }
}

@Composable
fun UserResultItem(
    user: User,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleBadgeAvatar(
                imageData = user.picture.medium,
                size = 38.dp,
                showBadge = isOnline
            )

            Spacer(modifier = Modifier.padding(4.dp))

            ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
                Text(
                    "${user.name.first} ${user.name.last}",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}
