package com.example.fellowship.ui.maincontent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.indelible.fellowship.AppState
import com.indelible.fellowship.navigation.AuthNavGraph
import com.indelible.fellowship.navigation.BottomNavItem
import com.indelible.fellowship.rememberAppStates
import com.indelible.fellowship.shouldShownBottomBar

@Composable
fun MainFragment(
    startDestination: String
){

    val appState = rememberAppStates()
    val shouldShowBottomBar = shouldShownBottomBar(appState)

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = SnackbarHostState(),
                modifier = Modifier.padding(8.dp),
                snackbar = { snackBarData ->
                    Snackbar(snackbarData = snackBarData,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        },
        modifier = Modifier.systemBarsPadding(),
        bottomBar = {
                BottomBar(appState, shouldShowBottomBar)
        },
    ) { paddingValues ->
        AuthNavGraph(appState, paddingValues, startDestination)
    }

}


@Composable
fun BottomBar(
    appState: AppState,
    shouldShowBottomBar: Boolean
){
    val screens = listOf(
        BottomNavItem.Messages,
        BottomNavItem.Calls,
        BottomNavItem.Stories,
        BottomNavItem.Profile
    )

    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = shouldShowBottomBar,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar() {
            screens.forEach { item ->
                NavigationBarItem(
                    label = { Text(text = stringResource(id = item.title))},
                    icon = {
                        Icon(imageVector = item.icon,
                            contentDescription = null)
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true,
                    onClick = { appState.navigate(item.route) }
                )
            }
        }
    }


}


//val LazyListState.elevation: Dp
//    get() = if (firstVisibleItemIndex == 0){
//        minOf(firstVisibleItemScrollOffset.toFloat().dp,
//            AppBarDefaults.TopAppBarElevation)
//    }else{
//        AppBarDefaults.TopAppBarElevation
//    }



