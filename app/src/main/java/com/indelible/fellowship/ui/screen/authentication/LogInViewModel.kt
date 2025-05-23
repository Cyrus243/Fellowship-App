package com.indelible.fellowship.ui.screen.authentication

import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
): AuthViewModel() {

    fun checkStatus(navigateAndPopUp:(Any, Any) -> Unit){
        if (accountService.hasUser){
            navigateAndPopUp(Destination.Messages, Destination.SignIn)
        }
    }

     fun onSignInClick(openAndPopUp:(Any, Any) -> Unit){
        super.onConnect(openAndPopUp)

        launchCatching {
            accountService.authenticate(userEmail, userPassword)
            openAndPopUp(Destination.Messages, Destination.SignIn)
        }

    }

    fun enableLogInButton(): Boolean =
        userEmail.isNotBlank() && userPassword.isNotBlank()

    fun onSignUpClick(navigate: (Any) -> Unit){
        navigate(Destination.SignUp)
    }
    companion object{
        private const val TAG = "SignInViewModel"
    }
}