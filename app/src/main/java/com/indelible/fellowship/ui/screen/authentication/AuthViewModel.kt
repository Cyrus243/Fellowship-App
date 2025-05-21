package com.indelible.fellowship.ui.screen.authentication

import com.indelible.fellowship.BaseViewModel
import com.indelible.fellowship.R
import com.indelible.fellowship.SignUpUIState
import com.indelible.fellowship.core.domain.isValidEmail
import com.indelible.fellowship.ui.component.SnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class AuthViewModel: BaseViewModel() {

    protected val _uiState = MutableStateFlow(SignUpUIState())
    val uiState = _uiState.asStateFlow()

    protected val userEmail
        get() = uiState.value.email

    protected val userPassword
        get() =  uiState.value.passWord


    fun updateUserEmail(email: String){
        _uiState.update { uiState.value.copy(email = email) }
    }

    fun updatePassWord(passWord: String){
        _uiState.update { uiState.value.copy(passWord = passWord) }
    }

    open fun onConnect(openAndPopUp:(String, String) -> Unit){
        clearError()

        if (!userEmail.isValidEmail()){
            SnackbarManager.showMessage(R.string.email_error)
            _uiState.update { uiState.value.copy(isNotValidEmail = true)}
            return
        }

        if(userPassword.isBlank()){
            SnackbarManager.showMessage(R.string.empty_password_error)
            _uiState.update { uiState.value.copy(isNotValidPassWord = true) }
            return
        }
    }

    private fun clearError(){
        _uiState.update {
            uiState.value.copy(
                isNotValidPassWord = false,
                isNotValidEmail = false,
                isNameEmpty = false,
            )
        }
    }

    fun onVisibilityClick(){
        _uiState.update {
            uiState.value.copy(isPasswordVisible = !uiState.value.isPasswordVisible)
        }
    }
}