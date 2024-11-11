package com.example.toolsfordriver.ui.screens.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.isValidPassword
import com.example.toolsfordriver.data.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetScreenViewModel @Inject constructor(
    private val auth: AccountService
) : ViewModel() {
    private val snackbarChannel = Channel<UiText>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()

    fun resetPassword(oobCode: String, password: String, onSuccess: () -> Unit) {
        if (!password.isValidPassword()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.password_error))
            }
            return
        }

        launchCatching { auth.resetPassword(oobCode, password) { onSuccess() } }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler{ _, throwable ->
                viewModelScope.launch {
                    val throwableMessage = UiText.DynamicString(throwable.message.toString())
                    snackbarChannel.send(throwableMessage)
                }
            },
            block = block
        )
    }
}