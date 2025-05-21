package com.indelible.fellowship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indelible.fellowship.ui.component.SnackbarManager
import com.indelible.fellowship.ui.component.SnackbarMessage.Companion.toSnackBarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(): ViewModel() {
    fun launchCatching(snackBar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch (
            CoroutineExceptionHandler { _, throwable ->
                if(snackBar){
                    SnackbarManager.showMessage(throwable.toSnackBarMessage())
                }
            },
            block = block
        )
}