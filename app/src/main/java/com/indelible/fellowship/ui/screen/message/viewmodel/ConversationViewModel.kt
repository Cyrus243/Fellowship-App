package com.indelible.fellowship.ui.screen.message.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.viewModelScope
import com.indelible.fellowship.BaseViewModel
import com.indelible.fellowship.ConversationState
import com.indelible.fellowship.core.model.Message
import com.indelible.fellowship.core.model.MessageStatus
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.model.UserNotification
import com.indelible.fellowship.core.service.MessageService
import com.indelible.fellowship.core.service.NotificationRepository
import com.indelible.fellowship.core.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val messageService: MessageService,
    private val storageService: StorageService,
    private val repository: NotificationRepository
): BaseViewModel() {

    val currentUserId = messageService.currentUserId
    private val _uiState = MutableStateFlow(ConversationState())
    val uiState = _uiState.asStateFlow()

    private val counter: Int
        get() = _uiState.value.counter

    fun onItemSelected(messageId: Int) {
        if (_uiState.value.selectedItemList.contains(messageId)) return
        _uiState.update {
            uiState.value.copy(
                selectedItemList = it.selectedItemList.toMutableList().also { list ->
                    list.add(messageId)
                }
            )
        }
    }

    fun onItemUnselected(messageId: Int) {
        if (!_uiState.value.selectedItemList.contains(messageId)) return
        _uiState.update {
            uiState.value.copy(
                selectedItemList = it.selectedItemList.toMutableList().also { list ->
                    list.remove(messageId)
                }
            )
        }
    }

    fun clearSelectedList() {
        _uiState.update {
            uiState.value.copy(
                selectedItemList = emptyList()
            )
        }
    }

    fun updateMessageField(message: String){
        _uiState.update {
            uiState.value.copy(message = message)
        }
    }

    fun updateOpenDialog(openDialog: Boolean){
        _uiState.update {
            uiState.value.copy(openDialog = openDialog)
        }
    }

    fun updateMessageSelection(selected: Boolean, message: Message){
        _uiState.update {
            uiState.value.copy(isMessageSelected = selected, replyMessage = message)
        }
    }

    fun startTimer(isRecording: Boolean){
        launchCatching(snackBar = false) {
            while (isRecording){
                delay(1000)
                _uiState.update {
                    uiState.value.copy(counter = counter + 1)
                }
            }
        }
    }
    fun resetScroll(lastIndex: Int){
        launchCatching(snackBar = false) {

        }
    }

    fun uploadMediaFile(
        uri: Uri,
        chatRoomId: String,
        opponentId: String,
        userName: String,
        opponent: User,
    ){
        val path = "$currentUserId/media/$chatRoomId/${UUID.randomUUID()}"
        launchCatching(snackBar = false) {
            storageService.uploadFile(
                uri = uri,
                path = path,
            ){
                postMessage(
                    chatRoomId = chatRoomId,
                    opponentId = opponentId,
                    opponent = opponent,
                    isMedia = true,
                    userName = userName,
                    mediaContent = it
                )
            }
        }
    }

    private fun postNotification(
        message: String,
        timestamp: Long,
        userName: String,
        opponent: User
    ){
        val notification = UserNotification(
            title = userName,
            message = message,
            author = userName,
            imagePath = opponent.photoPath,
            timestamp = timestamp,
            userId = opponent.userId
        )
        //TODO: Change this method later for a launch catching
        viewModelScope.launch {
            val response = repository.postNotification(notification, opponent.token)
            Log.d(TAG, "postNotification: $response")

        }

    }

    fun getConversation(chatRoomId: String): Flow<List<Message>>{
        return messageService.loadMessages(chatRoomId)
    }

    fun getOpponent(opponentId: String): Flow<User?> =
        messageService.loadUser(opponentId)

    fun postMessage(
        chatRoomId: String,
        opponentId: String,
        userName: String,
        opponent: User,
        isMedia: Boolean,
        mediaContent: String = ""
    ){
        val temp = _uiState.value.message.ifBlank { mediaContent }
        val timestamp = System.currentTimeMillis()
        val opponentVersion = Message(
            author = currentUserId,
            timestamps = timestamp,
            content = temp,
            messageStatus = MessageStatus.RECEIVED,
            media = isMedia
        )

        val userVersion = Message(
            author = "me",
            timestamps = timestamp,
            content = temp,
            media = isMedia
        )

        launchCatching {
            messageService.postMessage(
                message = userVersion,
                chatRoomId = chatRoomId,
                userId = currentUserId,
                opponentId = opponentId
            ){}
            messageService.postMessage(
                message = opponentVersion,
                chatRoomId = chatRoomId,
                userId = opponentId,
                opponentId = currentUserId
            ){
                Log.d(TAG, "postMessage in notification: $temp")
//                postNotification(
//                    message = temp,
//                    opponent = opponent,
//                    timestamp = timestamp,
//                    userName = userName
//                )
            }
        }
        updateMessageField("")
    }

    fun changeMessageStatus(message: Message, chatRoomId: String){
        launchCatching {
            if (message.messageStatus == MessageStatus.RECEIVED){
                messageService.updateMessage(
                    message.copy(messageStatus = MessageStatus.READ),
                    chatRoomId
                )
            }
        }
    }

    fun copyText(context: Context, text: String){
        val clipboardManager = context.getSystemService<ClipboardManager>()
        clipboardManager?.setPrimaryClip(
            ClipData.newPlainText("userMessage", text)
        )
    }

    fun deleteMessage(messages: List<Message>, chatRoomId: String){
        launchCatching(snackBar = false) {
            for (index in uiState.value.selectedItemList)
                messageService.deleteMessage(messages[index], chatRoomId)
        }
    }

    fun deleteConversation(chatRoomId: String){
        launchCatching {
            messageService.deleteConversation(chatRoomId)
        }
    }

    private fun createConversation(
        opponentId: String,
        msg: Message,
        chatRoomId: String,
        userId: String
    ){
        launchCatching {
            messageService.createConversation(opponentId, msg, chatRoomId, userId)
        }
    }
}

private const val TAG = "ConversationViewModel"