package com.indelible.fellowship.core.service.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.indelible.fellowship.core.model.Conversation
import com.indelible.fellowship.core.model.Message
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.service.MessageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessageServiceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseMessaging: FirebaseMessaging
): MessageService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val loadConversation: Flow<List<Conversation>>
    get() = currentCollection().snapshots().map { snapshot ->
            snapshot.toObjects(Conversation::class.java)
        }


    override val loadUsersList: Flow<List<User>>
        get() = fireStore.collection("Users")
            .whereNotEqualTo(USERID_FIELD, currentUserId)
            .snapshots().map { snapshot ->
                snapshot.toObjects(User::class.java)
            }

    override fun loadMessages(chatRoomId: String): Flow<List<Message>>{
        return currentCollection().document(chatRoomId)
            .collection(MESSAGE_COLLECTION).snapshots().map {
                it.toObjects(Message::class.java)
            }

    }

    override fun loadUser(opponentId: String): Flow<User?> =
        fireStore.collection("Users")
            .document(opponentId)
            .snapshots().map {
            it.toObject(User::class.java)
        }

    override suspend fun addUser(
        username: String,
        email: String,
        uid: String
    ) {
        val user = User(uid, email, username)
        fireStore.collection("Users")
            .add(user)
    }

    override suspend fun createConversation(
        opponentId: String,
        msg: Message,
        chatRoomId: String,
        currentUserId: String
    ) {
        val conversation = Conversation(
            opponentID = opponentId,
            lastMessage = msg,
            chatRoomId = chatRoomId)
        getPath(currentUserId).document(chatRoomId).set(conversation).await()
       // firebaseMessaging.subscribeToTopic(chatRoomId).await()
    }

    override suspend fun postMessage(
        message: Message,
        chatRoomId: String,
        userId: String,
        opponentId: String,
        onComplete: ()-> Unit
    ){

        getPath(userId)
            .document(chatRoomId)
            .collection(MESSAGE_COLLECTION)
            .add(message).addOnCompleteListener {
                if (it.isSuccessful)
                    onComplete()
            }.await()

        getPath(userId)
            .document(chatRoomId)
            .set(Conversation(lastMessage = message, opponentID = opponentId)).await()

    }

    override suspend fun deleteMessage(message: Message, chatRoomId: String) {
        currentCollection()
            .document(chatRoomId)
            .collection(MESSAGE_COLLECTION)
            .document(message.messageId).delete().await()

    }

    private fun createMessage(message: String, opponentToken: String): RemoteMessage =
        RemoteMessage.Builder(opponentToken)
            .addData("message",message)
            .setMessageId("0")
            .build()


    override suspend fun updateMessage(message: Message, chatRoomId: String) {
        currentCollection()
            .document(chatRoomId)
            .collection(MESSAGE_COLLECTION)
            .document(message.messageId).set(message).await()

        currentCollection()
            .document(chatRoomId).update("lastMessage", message).await()

    }

    override suspend fun deleteConversation(chatRoomId: String){
        currentCollection().document(chatRoomId).delete().await()
    }

    private fun currentCollection(): CollectionReference =
        fireStore.collection( "$ALL_CONVERSATION_COLLECTION/$currentUserId/$CONVERSATION_COLLECTION")

    private fun getPath(uid: String): CollectionReference =
        fireStore.collection("$ALL_CONVERSATION_COLLECTION/$uid/$CONVERSATION_COLLECTION")


    companion object{
        private const val ALL_CONVERSATION_COLLECTION = "AllConversations"
        private const val CONVERSATION_COLLECTION = "Conversations"
        private const val TAG = "MessageService"
        private const val MESSAGE_COLLECTION = "MessagesCollections"
        private const val USERID_FIELD = "userId"
    }

}



