package com.se7en.jetmessenger.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.se7en.jetmessenger.data.models.User
import com.se7en.jetmessenger.ui.components.CircleBadgeAvatar
import com.se7en.jetmessenger.ui.components.SubHeading
import com.se7en.jetmessenger.ui.components.UserAvatarWithTitle
import com.se7en.jetmessenger.ui.screens.Routing
import com.se7en.jetmessenger.viewmodels.UsersViewModel

// TODO: No results
@Composable
fun Routing.Search.Content(
    usersViewModel: UsersViewModel,
    onBackPress: () -> Unit,
    onUserClick: (user: User) -> Unit
) {
    val (query, setQuery) = remember { mutableStateOf("") }
    val results: List<User> by usersViewModel.searchUsers(query).collectAsState(listOf())
    val recentSearches: List<User> = usersViewModel.recentSearches.reversed()
    val suggestedSearches: List<User> by usersViewModel.suggestedSearches.collectAsState(listOf())

    Scaffold(
        topBar = {
            TopBar(onBackPress = onBackPress, onSearch = setQuery)
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if(query.isBlank()) {
                if(recentSearches.isNotEmpty()) {
                    item { SubHeading("RECENT SEARCHES") }
                    item { RecentSearchesRow(recentSearches, onUserClick) }
                }
                item { SubHeading("SUGGESTED") }
                itemsIndexed(suggestedSearches) { index, user ->
                    UserColumnItem(
                        user = user,
                        isOnline = index % 3 == 0,
                        modifier = Modifier.clickable(onClick = { onUserClick(user) })
                    )
                }
            } else {
                itemsIndexed(results) { index, user ->
                    UserColumnItem(
                        user = user,
                        isOnline = index % 3 == 0,
                        modifier = Modifier.clickable(onClick = {
                            onUserClick(user)
                            usersViewModel.addToRecentSearches(user)
                        }),
                    )
                }
            }
        }
    }
}

@Composable
fun RecentSearchesRow(
    users: List<User>,
    onItemClick: (user: User) -> Unit
) {
    LazyRowForIndexed(
        items = users,
        contentPadding = PaddingValues(2.dp)
    ) { index, user ->
        val padding = 4.dp
        val (startPadding, endPadding) = when(index) {
            0 -> Pair(8.dp, padding)
            users.lastIndex -> Pair(padding, 8.dp)
            else -> Pair(padding, padding)
        }

        UserAvatarWithTitle(
            title = user.name.first,
            imageData = user.picture.medium,
            imageSize = 56.dp,
            modifier = Modifier
                .clickable(onClick = { onItemClick(user) })
                .padding(
                    start = startPadding,
                    end = endPadding,
                    top = padding,
                    bottom = padding
                )
        )
    }
}

@Composable
fun UserColumnItem(
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

            Providers(AmbientContentAlpha provides ContentAlpha.high, children = {
                Text(
                    "${user.name.first} ${user.name.last}",
                    style = MaterialTheme.typography.subtitle1
                )
            })
        }
    }
}
