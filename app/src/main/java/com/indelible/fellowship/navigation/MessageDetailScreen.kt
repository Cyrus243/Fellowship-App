package com.indelible.fellowship.navigation

sealed class MessageDetailScreens(
    val title: String,
    val opponentIdArgs: String,
    val chatRoomIdArgs: String,
    val route: String
    ){

    object MessageDetailScreen: MessageDetailScreens(
        chatRoomIdArgs = "/{chatRoomUUID}",
        opponentIdArgs =  "/{opponentUUID}",
        title = "Message Detail",
        route = "message_details"
    ){
        val startRoute = route + opponentIdArgs
        val fullRoute = route + chatRoomIdArgs + opponentIdArgs
    }
}
