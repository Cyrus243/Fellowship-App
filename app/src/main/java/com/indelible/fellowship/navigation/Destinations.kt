package com.indelible.fellowship.navigation

import kotlinx.serialization.Serializable

sealed class GraphRoute {
    @Serializable
    object Root

    @Serializable
    object MessageDetails
}


sealed class Destination {
    @Serializable
    object SignIn

    @Serializable
    object SignUp

    @Serializable
    object SplashScreen

    @Serializable
    object Messages

    @Serializable
    object Calls

    @Serializable
    object Stories

    @Serializable
    object Profile

    @Serializable
    object StartChat

    @Serializable
    object EditProfile


    @Serializable
    data class MessageDetails(val chatRoomId: String, val opponentId: String)

    @Serializable
    data class ImageViewer(val imagePath: String)
}

