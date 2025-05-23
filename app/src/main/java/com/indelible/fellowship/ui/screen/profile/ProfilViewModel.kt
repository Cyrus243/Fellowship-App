package com.indelible.fellowship.ui.screen.profile

import android.net.Uri
import com.indelible.fellowship.BaseViewModel
import com.indelible.fellowship.core.domain.PROFILE_IMAGE_PATH
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.core.service.MessageService
import com.indelible.fellowship.core.service.StorageService
import com.indelible.fellowship.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val messageService: MessageService,
    private val storageService: StorageService
): BaseViewModel(){

    val user = messageService.loadUser(accountService.currentUserId)

    fun uploadProfileImage(uri: Uri){
        val path = "${accountService.currentUserId}/$PROFILE_IMAGE_PATH"
        launchCatching(snackBar = false) {
            storageService.uploadFile(
                uri = uri,
                path = path,
            ){
              updateProfileImageUrl(it)
            }
        }
    }

    private fun updateProfileImageUrl(url: String){
        launchCatching {
            accountService.updateImageUrl(url)
        }
    }

    fun onLogOutClick(openAndPopUp:(Any, Any) -> Unit){
        launchCatching {
            accountService.signOut()
            openAndPopUp(Destination.SignIn, Destination.Profile)
        }
    }

    fun updateUser(user: User){
        launchCatching {
            accountService.updateUser(user)
        }
    }
}