package com.indelible.fellowship.core.model

data class UserNotification(
    val title: String = "",
    val imagePath: String ="",
    val author: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val userId: String = ""
)
