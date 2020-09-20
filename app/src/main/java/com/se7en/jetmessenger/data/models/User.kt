package com.se7en.jetmessenger.data.models

data class UserDataWrapper(
    val info: Info,
    val results: List<User>
)

data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)

data class User(
    val name: Name,
    val picture: Picture
)

data class Name(
    val first: String,
    val last: String,
    val title: String
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)
