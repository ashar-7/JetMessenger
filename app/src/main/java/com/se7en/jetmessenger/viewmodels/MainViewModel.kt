package com.se7en.jetmessenger.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.se7en.jetmessenger.data.UserApiService
import com.se7en.jetmessenger.data.models.User

class MainViewModel: ViewModel() {
    // TODO: Error handling

    private val apiService = UserApiService.create()

    val users = liveData {
        emit(getUsers(20))
    }

    private suspend fun getUsers(count: Int): List<User> {
        val data = apiService.getUsers(count)
        return data.results
    }
}
