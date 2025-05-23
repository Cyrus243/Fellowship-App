package com.indelible.fellowship.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indelible.fellowship.ui.screen.callcontent.CallFragment
import com.indelible.fellowship.AppState
import com.indelible.fellowship.ui.screen.message.MessageFragment
import com.indelible.fellowship.ui.screen.profile.ProfileFragment


fun NavGraphBuilder.mainNavGraph(
    appState: AppState,
    paddingValues: PaddingValues
) {

    navigation<GraphRoute.Root>(startDestination = Destination.Messages) {

        composable<Destination.Messages> {
            MessageFragment(
                modifier = Modifier,
                navigate = appState::navigate,
                navigateAndPopUp = appState::navigateAndPopUp,
            )
        }

        composable<Destination.Calls> {
            CallFragment()
        }

        composable<Destination.Profile> {
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

        composable<Destination.Stories> {
            //StoriesFragment()
        }


        composable<Destination.EditProfile>{
//            EditProfile(
//                onPopUp = { appState.popUp() }
//            )
        }

        messageNavGraph(appState)
    }
}

const val TAG = "MainNavGraph"