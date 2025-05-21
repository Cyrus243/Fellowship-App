package com.indelible.fellowship.core.service

import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.model.UserStatus

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun createAccount(email: String, password: String, name: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
    suspend fun changeUserStatus(userStatus: UserStatus)
    suspend fun updateUser(user: User)
    suspend fun updateUserToken(token: String)
    suspend fun getCurrentToken(sendRegistrationToServer:(String) -> Unit)

    suspend fun updateImageUrl(imageUrl: String)


}