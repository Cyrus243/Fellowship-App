package com.indelible.fellowship.core.model

data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val photoPath: String = "",
    val biography: String = "",
    val status: UserStatus = UserStatus.OFFLINE,
    val token: String = ""
)