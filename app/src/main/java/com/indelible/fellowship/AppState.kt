package com.indelible.fellowship

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.indelible.fellowship.core.model.Message
import com.indelible.fellowship.navigation.BottomNavItem
import com.indelible.fellowship.ui.component.SnackbarManager
import com.indelible.fellowship.ui.component.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Composable
fun rememberAppStates(
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackbarManager = SnackbarManager,
    snackBarHostState: SnackbarHostState = SnackbarHostState(),
    resources: Resources = resources(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(navController, snackBarHostState, snackBarManager, resources, systemUiController, coroutineScope){
        AppState(
            navController = navController,
            snackBarHostState = snackBarHostState,
            snackBarManager = snackBarManager,
            resources = resources,
            systemUiController = systemUiController,
            coroutineScope = coroutineScope
        )
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

class AppState constructor(
    val navController: NavHostController,
    private val snackBarHostState: SnackbarHostState,
    private val resources: Resources,
    private val snackBarManager: SnackbarManager,
    val systemUiController: SystemUiController,
    val coroutineScope: CoroutineScope
){

    init {
        coroutineScope.launch {
            snackBarManager.snackbarMessage.filterNotNull().collect { snackBarMessage ->
                val text = snackBarMessage.toMessage(resources)
                snackBarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp(){
        navController.popBackStack()
    }
    fun navigate(screen: Any){
        navController.navigate(screen){
            launchSingleTop = true
        }
    }

    fun navigateAndPopUp(route: String, popUp: String){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo(popUp){
                inclusive = true
            }
        }
    }

    fun clearAndNavigate(route: String){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo(0){inclusive = true}
        }
    }

}

@Composable
fun shouldShownBottomBar(appState: AppState) =
    appState.navController.currentBackStackEntryAsState()
        .value?.destination?.route in bottomNavRoutes

private val bottomNavRoutes = BottomNavItem.entries.map { it.route }


// LogIn Screen UI State

data class SignInUIState(
    val email: String = "",
    val passWord: String = "",
    val isNotValidEmail: Boolean = false,
    val isNotValidPassWord: Boolean = false,
    val isPasswordVisible: Boolean = false
)

// Sign Up Screen UI State

data class SignUpUIState(
    val email: String = "",
    val passWord: String = "",
    val name: String = "",
    val isNameEmpty: Boolean = false,
    val isNotValidEmail: Boolean = false,
    val isNotValidPassWord: Boolean = false,
    val isPasswordVisible: Boolean = false
)

data class MessageItemState(
    val searchText: String = "",
    val active: Boolean = false,
    val revealedItemIdsList: List<Int> = emptyList()
)


data class ConversationState(
    val selectedItemList: List<Int> = emptyList(),
    val message: String = "",
    val openDialog: Boolean = false,
    val isMessageSelected: Boolean = false,
    val replyMessage: Message = Message(),
    val counter: Int = 0,
    val isRecording: Boolean = false
)

data class EditProfileState(
    val textField: String = ""
)
