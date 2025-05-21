package com.indelible.fellowship.ui.component

import android.content.res.Resources
import androidx.annotation.StringRes
import com.indelible.fellowship.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class SnackbarMessage{
    class StringSnackbar(val message: String): SnackbarMessage()
    class ResourceSnackbar(@StringRes val message: Int): SnackbarMessage()

    companion object{
        fun SnackbarMessage.toMessage(ressoures: Resources): String{
            return when(this){
                is StringSnackbar -> this.message
                is ResourceSnackbar -> ressoures.getString(this.message)
            }
        }
        fun Throwable.toSnackBarMessage(): SnackbarMessage{
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(R.string.generic_error)
        }
    }
}

object SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessage: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(@StringRes message: Int){
        messages.value = SnackbarMessage.ResourceSnackbar(message)
    }

    fun showMessage(message: SnackbarMessage){
        messages.value = message
    }
}

