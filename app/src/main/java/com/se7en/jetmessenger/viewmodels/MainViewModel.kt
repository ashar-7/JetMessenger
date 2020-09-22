package com.se7en.jetmessenger.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.se7en.jetmessenger.data.UserApiService
import com.se7en.jetmessenger.data.models.User
import kotlinx.coroutines.flow.map

class MainViewModel: ViewModel() {
    // TODO: Error handling

    private val apiService = UserApiService.create()

    val users = liveData {
        emit(getUsers(20))
    }

    fun searchUsers(query: String) =
        users.asFlow().map {  users ->
            if(query.isNotBlank())
                users.filter { user -> "${user.name.first} ${user.name.last}".contains(query) }
            else listOf()
        }

    private suspend fun getUsers(count: Int): List<User> {
        val data = apiService.getUsers(count)
        return data.results
    }
}
