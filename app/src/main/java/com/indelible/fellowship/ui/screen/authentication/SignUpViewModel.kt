package com.example.fellowship.ui.authentication

import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.core.service.MessageService
import com.indelible.fellowship.navigation.Destination
import com.indelible.fellowship.ui.screen.authentication.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val messageService: MessageService
): AuthViewModel() {

    private val userName
        get() = uiState.value.name

    fun onPopUp(popUp: () -> Unit){
        popUp()
    }

    fun updateUserName(name: String){
        _uiState.update {
            uiState.value.copy(name = name)
        }
    }
    fun onCreateAccount(openAndPopUp: (Any, Any) -> Unit){
        super.onConnect(openAndPopUp)

        launchCatching {
            accountService.createAccount(
                email = userEmail,
                password = userPassword,
                name = userName
            )
            openAndPopUp(Destination.Messages, Destination.SignUp)
        }
    }

}