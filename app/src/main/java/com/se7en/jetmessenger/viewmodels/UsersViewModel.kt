package com.se7en.jetmessenger.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.se7en.jetmessenger.data.UserApiService
import com.se7en.jetmessenger.data.models.User
import kotlinx.coroutines.flow.map

class UsersViewModel: ViewModel() {
    // TODO: Error handling

    private val apiService = UserApiService.create()

    val users = liveData {
        emit(getUsers(20))
    }.asFlow()

    val recentSearches = mutableStateListOf<User>()
    val suggestedSearches = users.map {
        it.filterIndexed { index, _ -> index % 2 == 0 }
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

    private suspend fun getUsers(count: Int): List<User> {
        val data = apiService.getUsers(count)
        return data.results
    }
}
