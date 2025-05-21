package com.indelible.fellowship.core.model

import com.google.firebase.firestore.DocumentId

data class Conversation(
    val opponentID: String = "",
    val lastMessage: Message = Message(),
    @DocumentId val chatRoomId: String = ""
)

