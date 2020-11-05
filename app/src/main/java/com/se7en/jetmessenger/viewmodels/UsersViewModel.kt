package com.se7en.jetmessenger.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se7en.jetmessenger.data.UserApiService
import com.se7en.jetmessenger.data.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModel: ViewModel() {
    // TODO: Error handling

    private val apiService = UserApiService.create()

    private val _users by lazy {
        MutableStateFlow<List<User>>(listOf()).also { usersFlow ->
            viewModelScope.launch {
                usersFlow.value = fetchUsers(count = 20)
            }
        }
    }
    val users: StateFlow<List<User>> = _users

    val recentSearches = mutableStateListOf<User>()
    val suggestedSearches by lazy {
        users.map {
            it.filterIndexed { index, _ -> index % 2 == 0 }
        }
    }

    fun getUser(userId: String): Flow<User> = users.mapNotNull {
        it.find { user -> user.id == userId }
    }

    fun searchUsers(query: String) =
        users.map {  users ->
            return@map if(query.isNotBlank())
                users.filter { user ->
                    "${user.name.first} ${user.name.last}".contains(
                        query, ignoreCase = true
                    )
                }
            else listOf()
        }

    fun addToRecentSearches(user: User) {
        recentSearches.removeAll { it == user }
        recentSearches.add(user)
    }

    private suspend fun fetchUsers(count: Int): List<User> {
        val data = apiService.getUsers(count)
        return data.results
    }
}
