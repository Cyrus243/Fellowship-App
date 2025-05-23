package com.indelible.fellowship.ui.screen.maincontent

import com.indelible.fellowship.BaseViewModel
import com.indelible.fellowship.core.model.UserStatus
import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.navigation.Destination
import com.indelible.fellowship.navigation.GraphRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountService: AccountService
): BaseViewModel() {

    fun getStarDestination(): Any {
       return if (accountService.hasUser)
            GraphRoute.Root
        else
            Destination.SignIn
    }

    fun setUserStatusToFireBase(userStatus: UserStatus){
        launchCatching(snackBar = false) {
            accountService.changeUserStatus(userStatus)
        }
    }
}