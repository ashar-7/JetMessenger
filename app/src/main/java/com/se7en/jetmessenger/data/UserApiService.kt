package com.se7en.jetmessenger.data

import com.se7en.jetmessenger.data.models.UserDataWrapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("?inc=name,picture,phone")
    suspend fun getUsers(
        @Query("results") count: Int
    ): UserDataWrapper

    companion object {
        fun create(): UserApiService = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }
}
