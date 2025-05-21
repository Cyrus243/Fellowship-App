package com.indelible.fellowship.ui.screen.authentication

import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
): AuthViewModel() {

    fun checkStatus(navigateAndPopUp:(String, String) -> Unit){
        if (accountService.hasUser){
            navigateAndPopUp(Graph.HOME, Graph.SIGN_IN_CONTENT)
        }
    }

     fun onSignInClick(openAndPopUp:(String, String) -> Unit){
        super.onConnect(openAndPopUp)

        launchCatching {
            accountService.authenticate(userEmail, userPassword)
            openAndPopUp(Graph.HOME, Graph.SIGN_IN_CONTENT)
        }

    }

    fun enableLogInButton(): Boolean =
        userEmail.isNotBlank() && userPassword.isNotBlank()

    fun onSignUpClick(navigate: (String) -> Unit){
        navigate(Graph.SIGN_UP_CONTENT)
    }
    companion object{
        private const val TAG = "SignInViewModel"
    }
}