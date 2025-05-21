package com.indelible.fellowship.core.service.impl


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.model.UserStatus
import com.indelible.fellowship.core.service.AccountService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging
    ): AccountService {

    override suspend fun getCurrentToken(sendRegistrationToServer:(String) -> Unit) {
        firebaseMessaging.token.addOnCompleteListener {
            if (it.isSuccessful){
                sendRegistrationToServer(it.result)
            }
        }
    }

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override suspend fun changeUserStatus(userStatus: UserStatus){
        currentCollection()
            .document(currentUserId)
            .update(USER_STATUS, userStatus).await()

    }

    override suspend fun updateUser(user: User) {
        currentCollection()
            .document(currentUserId)
            .set(user).await()
    }

    override suspend fun updateUserToken(token: String) {
        currentCollection()
            .document(currentUserId)
            .update(USER_TOKEN_FIELD, token).await()
    }


    override suspend fun updateImageUrl(imageUrl: String) {
        currentCollection()
            .document(currentUserId)
            .update("photoPath", imageUrl).await()
    }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {}

    override suspend fun createAnonymousAccount() {}

    override suspend fun createAccount(email: String, password: String, name: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val profileUpdate = UserProfileChangeRequest.Builder()
                    profileUpdate.displayName = name
                    auth.currentUser!!.updateProfile(profileUpdate.build())
                    val user = User(userId = currentUserId, email = email, name = name)
                    fireStore.collection("Users").document(currentUserId).set(user)
                }
            }.await()

}

    override suspend fun linkAccount(email: String, password: String) {}

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous){
            auth.currentUser!!.delete()
        }
        auth.signOut()

        //recall authentication screen
    }

    private fun currentCollection(): CollectionReference =
        fireStore.collection( "$USERS_COLLECTION")

    companion object{
        private const val USER_STATUS = "status"
        private const val USERS_COLLECTION = "Users"
        private const val TAG = "AccountService"
        private const val USER_TOKEN_FIELD = "token"
    }
}

