package com.indelible.fellowship.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.indelible.fellowship.ui.screen.authentication.LogInContent
import com.indelible.fellowship.ui.screen.authentication.SignUpContent
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.indelible.fellowship.AppState
import com.indelible.fellowship.core.domain.navigateAndPopUp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthNavGraph(
    appState: AppState,
    paddingValues: PaddingValues,
    starDestination: String
){
    val navController = appState.navController
    AnimatedNavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination =  starDestination,
        modifier = Modifier
    ){
        composable(Graph.SPLASH_SCREEN){
//            SplashScreen{ route, popUp ->
//                navController.navigateAndPopUp(route, popUp)
//            }
        }
        composable(Graph.SIGN_IN_CONTENT){
            LogInContent(
                openAndPopUp = { route, popUp ->
                    navController.navigateAndPopUp(route, popUp)
                },
                navigate = {route -> navController.navigate(route) }
            )
        }
        composable(Graph.SIGN_UP_CONTENT){
            SignUpContent(
                openAndPopUp = { route, popUp ->
                    navController.navigateAndPopUp(route, popUp)
                },
                popUp = { navController.popBackStack() }
            )
        }
        mainNavGraph(appState, paddingValues)

    }
}

