package com.example.toolsfordriver.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.isValidEmail
import com.example.toolsfordriver.common.isValidPassword
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.data.model.service.AccountService
import com.example.toolsfordriver.data.model.service.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: FirestoreService
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    private val snackbarChannel = Channel<UiText>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()

    fun isUserAuthenticated(): Boolean = accountService.hasUser

    fun onEmailChange(email: String) {
        _authUiState.value = _authUiState.value.copy(email = email)
    }
    fun onPasswordChange(password: String) {
        _authUiState.value = _authUiState.value.copy(password = password)
    }

    fun invertIsNewAccountValue() {
        _authUiState.value = _authUiState.value.copy(
            isNewAccount = !_authUiState.value.isNewAccount
        )
    }

    fun invertPasswordVisibilityValue() {
        _authUiState.value = _authUiState.value.copy(
            passwordVisibility = !_authUiState.value.passwordVisibility
        )
    }

    fun onSignUserClick(onAuthSuccess: () -> Unit) {
        val email = _authUiState.value.email
        val password = _authUiState.value.password

        if (email.isEmpty() && password.isEmpty()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.empty_email_and_password))
            }
            return
        }

        if (!email.isValidEmail()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.email_error))
            }
            return
        }

        if (!password.isValidPassword()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.password_error))
            }
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
//            if (accountService.hasUser) {
//                storageService.saveUser(User(userId = accountService.currentUserId))
//            }
            onAuthSuccess()
        }
    }

    fun onCreateNewUserClick(onAuthSuccess: () -> Unit) {
        val email = _authUiState.value.email
        val password = _authUiState.value.password

        if (email.isEmpty() && password.isEmpty()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.empty_email_and_password))
            }
            return
        }

        if (!email.isValidEmail()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.email_error))
            }
            return
        }

        if (!password.isValidPassword()) {
            viewModelScope.launch {
                snackbarChannel.send(UiText.StringResource(R.string.password_error))
            }
            return
        }

        launchCatching {
            accountService.createAccount(email, password)
            if (accountService.hasUser) {
                storageService.saveUser(User(userId = accountService.currentUserId))
            }
            onAuthSuccess()
        }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler{ _, throwable ->
                viewModelScope.launch {
                    val throwableMessage = UiText.DynamicString(throwable.message.toString())

                    snackbarChannel.send(
                        if (throwableMessage.str == AUTH_CREDENTIAL_ERROR_MESSAGE) {
                            UiText.StringResource(R.string.wrong_login_or_password)
                        } else throwableMessage
                    )
                }
            },
            block = block
        )
    }

    companion object {
        private const val AUTH_CREDENTIAL_ERROR_MESSAGE =
            "The supplied auth credential is incorrect, malformed or has expired."
    }
}

