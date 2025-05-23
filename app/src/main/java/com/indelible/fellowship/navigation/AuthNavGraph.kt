package com.indelible.fellowship.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.indelible.fellowship.AppState
import com.indelible.fellowship.ui.screen.authentication.LogInContent
import com.indelible.fellowship.ui.screen.authentication.SignUpContent


@Composable
fun AuthNavGraph(
    appState: AppState,
    paddingValues: PaddingValues,
    starDestination: Any
){
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = starDestination,
    ){
        composable<Destination.SplashScreen> {
//            SplashScreen { route, popUp ->
//                navController.navigateAndPopUp(route, popUp)
//            }
        }

        composable<Destination.SignIn> {
            LogInContent(
                openAndPopUp = { route, popUp ->
                    appState.navigateAndPopUp(route, popUp)
                },
                navigate = { route -> appState.navigate(route) }
            )
        }

        composable<Destination.SignUp> {
            SignUpContent(
                openAndPopUp = { route, popUp ->
                    appState.navigateAndPopUp(route, popUp)
                },
                popUp = { navController.popBackStack() }
            )
        }
        mainNavGraph(appState, paddingValues)
    }
}

