package com.indelible.fellowship.navigation


object Graph{
    const val ROOT = "Root"
    const val AUTHENTICATION_CONTENT = "Authentication_graph"
    const val SIGN_IN_CONTENT = "Sign_in_graph"
    const val SIGN_UP_CONTENT = "Sign_up_graph"
    const val HOME = "Main_content"
    const val START_CHAT = "Start_chat"
    const val SPLASH_SCREEN = "Splash_screen"
    const val MAIN = "Main"
    const val MESSAGE_DETAILS = "Details_graph"
    const val EDIT_PROFILE = "Edit_profile"

}

sealed class GraphRoute {
    object Root
    object MessageDetails
}

sealed class Destination {
    object SignIn
    object SignUp
    object Main
    object Home
    object SplashScreen

    object Messages
    object Calls
    object Stories
    object Profile
    object StartChat
    object EditProfile

    data class MessageDetails(val chatRoomId: String, val opponentUUID: String)
    data class ImageViewer(val imagePath: String)
}

