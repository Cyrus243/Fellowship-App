package com.indelible.fellowship.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.fellowship.ui.callcontent.CallFragment
import com.indelible.fellowship.ui.screen.mediaviewer.ImageViewer
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.indelible.fellowship.AppState
import com.indelible.fellowship.ui.screen.message.ConversationScreen
import com.indelible.fellowship.ui.screen.message.MessageFragment
import com.indelible.fellowship.ui.screen.profile.ProfileFragment

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainNavGraph(
    appState: AppState,
    paddingValues: PaddingValues
) {

    navigation(
        route = Graph.HOME,
        startDestination = BottomNavItem.Messages.route
    ) {

        composable(
            route = BottomNavItem.Messages.route
        ) {
            MessageFragment(
                modifier = Modifier,
                navigate = { appState.navigate(it) }
            )
        }
        composable(route = BottomNavItem.Calls.route) {
            CallFragment()
        }
        composable(route = BottomNavItem.Profile.route) {
            ProfileFragment(
                openAndPopUp = { route, popUp ->
                    appState.navigateAndPopUp(route, popUp)
                },
                navigate = {
                    appState.navigate(it)
                },
                popUp = { appState.popUp() }
            )
        }

        composable(route = BottomNavItem.Stories.route) {
            //StoriesFragment()
        }

        composable(route = Graph.START_CHAT){
//            StartChatScreen(
//                navigateUp = { appState.popUp() },
//                navigateAndPopUp = { route, popUp ->
//                    appState.navigateAndPopUp(route, popUp)
//                }
//            )
        }
        navigation(
            route = Graph.MESSAGE_DETAILS,
            startDestination = MessageDetailScreens.MessageDetailScreen.route
        ) {
            composable(
                MessageDetailScreens.MessageDetailScreen.fullRoute,
                arguments = listOf(
                    navArgument("chatRoomUUID"){
                        type = NavType.StringType
                    },
                    navArgument("opponentUUID"){
                        type = NavType.StringType
                    }
                )
            ) {
                val opponentID = it.arguments?.getString("opponentUUID")
                val chatRoomId = it.arguments?.getString("chatRoomUUID")
                ConversationScreen(
                    navigate = { route -> appState.navigate(route) },
                    chatRoomId = chatRoomId ?: "",
                    opponentId = opponentID ?: "",
                    popUp = { appState.popUp() }
                )
            }

            composable(
                route = ImageViewerScreens.ImageViewerScreen.fullRoute,
                arguments = listOf(
                    navArgument("imagePath"){
                        type = NavType.StringType
                    }
                )
            ){
                val imagePath = it.arguments?.getString("imagePath")

                ImageViewer(
                    imageLink = imagePath ?: "",
                    navigateUp = { appState.popUp() }
                )
            }

        }



        composable(route = Graph.EDIT_PROFILE){
//            EditProfile(
//                onPopUp = { appState.popUp() }
//            )
        }
    }
}

const val TAG = "MainNavGraph"