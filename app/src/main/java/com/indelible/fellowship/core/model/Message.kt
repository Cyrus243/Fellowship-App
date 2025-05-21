package com.indelible.fellowship.core.model

import com.google.firebase.firestore.DocumentId


data class Message(
    @DocumentId val messageId: String = "",
    val author: String = "",
    val content: String = "",
    val media: Boolean = false,
    val timestamps: Long = 0,
    val messageStatus: MessageStatus = MessageStatus.READ
)

enum class MessageStatus{
    RECEIVED, READ
}