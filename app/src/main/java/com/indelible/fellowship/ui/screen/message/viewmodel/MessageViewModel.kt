package com.indelible.fellowship.ui.screen.message.viewmodel

import com.indelible.fellowship.BaseViewModel
import com.indelible.fellowship.MessageItemState
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.service.AccountService
import com.indelible.fellowship.core.service.MessageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    messageService: MessageService,
    private val accountService: AccountService,
): BaseViewModel() {

    private val _uiState = MutableStateFlow(MessageItemState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserToken()
    }

    val  messageRowItems = messageService.loadConversation
    val userList = messageService.loadUsersList
    val uid = accountService.currentUserId

    private val searchText: String
        get() = _uiState.value.searchText

    private val active: Boolean
        get() = _uiState.value.active

    private val revealedItemList: List<Int>
        get() = _uiState.value.revealedItemIdsList

    fun updateActiveSearch(active: Boolean){
        _uiState.update {
            uiState.value.copy(active = active)
        }
    }

    fun onItemExpanded(cardId: Int) {
        if (_uiState.value.revealedItemIdsList.contains(cardId)) return
        _uiState.update {
            uiState.value.copy(
                revealedItemIdsList = listOf(cardId)
            )
        }
    }

    fun onItemCollapsed(cardId: Int) {
        if (!_uiState.value.revealedItemIdsList.contains(cardId)) return
        _uiState.update {
            uiState.value.copy(
                revealedItemIdsList = it.revealedItemIdsList.toMutableList().also { list ->
                    list.remove(cardId) })
        }
    }

    fun updateSearchText(text:String) {
        _uiState.update {
            uiState.value.copy(searchText = text)
        }
    }
    fun getUser(userList: List<User>, opponentId: String): User?{
        return userList.find {
            it.userId == opponentId
        }
    }

    private fun getUserToken(){
        launchCatching {
           accountService.getCurrentToken {
               updateUserToken(it)
           }
        }
    }

    private fun updateUserToken(token: String){
        launchCatching {
            accountService.updateUserToken(token)
        }
    }
}

