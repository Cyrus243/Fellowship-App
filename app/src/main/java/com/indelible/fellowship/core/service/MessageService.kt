package com.indelible.fellowship.core.service

import com.indelible.fellowship.core.model.Conversation
import com.indelible.fellowship.core.model.Message
import com.indelible.fellowship.core.model.User
import kotlinx.coroutines.flow.Flow

interface MessageService {

    val currentUserId: String
    val loadConversation: Flow<List<Conversation>>
    val loadUsersList: Flow<List<User>>
    fun loadMessages(chatRoomId: String): Flow<List<Message>>
    fun loadUser(opponentId: String): Flow<User?>
    suspend fun addUser(username: String, email: String, uid: String)
    suspend fun createConversation(
        opponentId: String,
        msg: Message,
        chatRoomId: String,
        currentUserId: String
    )
    suspend fun postMessage(
        message: Message,
        chatRoomId: String,
        userId: String,
        opponentId: String,
        onComplete:()-> Unit
    )

    suspend fun deleteMessage(
        message: Message,
        chatRoomId: String
    )

    suspend fun updateMessage(message: Message, chatRoomId: String)
    suspend fun deleteConversation(chatRoomId: String)

}