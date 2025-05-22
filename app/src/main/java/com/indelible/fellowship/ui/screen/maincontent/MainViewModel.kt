package com.indelible.fellowship.ui.screen.maincontent

import com.indelible.fellowship.BaseViewModel
import com.indelible.fellowship.core.model.UserStatus
import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountService: AccountService
): BaseViewModel() {

    fun getStarDestination(): String {
       return if (accountService.hasUser)
            Graph.HOME
        else
            Graph.SIGN_IN_CONTENT
    }

    fun setUserStatusToFireBase(userStatus: UserStatus){
        launchCatching(snackBar = false) {
            accountService.changeUserStatus(userStatus)
        }
    }
}