package com.indelible.fellowship.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

import androidx.navigation.toRoute
import com.indelible.fellowship.AppState
import com.indelible.fellowship.ui.screen.mediaviewer.ImageViewer
import com.indelible.fellowship.ui.screen.message.ConversationScreen
import java.util.UUID


fun NavGraphBuilder.messageNavGraph(
    appState: AppState
){
    navigation<GraphRoute.MessageDetails>(startDestination = Destination.MessageDetails::class) {

        composable<Destination.MessageDetails> { entry ->
            val args: Destination.MessageDetails = entry.toRoute()

            ConversationScreen(
                navigate = { route -> appState.navigate(route) },
                chatRoomId = args.chatRoomId.ifEmpty { UUID.randomUUID().toString() },
                opponentId = args.opponentId,
                popUp = { appState.popUp() }
            )
        }

        composable<Destination.ImageViewer> { entry ->
            val args: Destination.ImageViewer = entry.toRoute()

            ImageViewer(
                imageLink = args.imagePath,
                navigateUp = { appState.popUp() }
            )
        }

    }
}