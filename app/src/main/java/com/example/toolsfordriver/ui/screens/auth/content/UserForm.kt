package com.example.toolsfordriver.ui.screens.auth.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.buttons.AppButton
import com.example.toolsfordriver.ui.common.text.PasswordInput
import com.example.toolsfordriver.ui.screens.auth.AuthScreenViewModel

@Composable
fun UserForm(
    viewModel: AuthScreenViewModel,
    onAuthSuccess: () -> Unit
) {
    val passwordFocusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    val authUiState = viewModel.authUiState.collectAsStateWithLifecycle().value
    val isCreateAccount = authUiState.isNewAccount
    val passwordState = rememberSaveable { mutableStateOf(authUiState.password) }

    LaunchedEffect(passwordState.value) { viewModel.updatePassword(passwordState.value) }

    Spacer(modifier = Modifier.height(150.dp))

    EmailInput(
        viewModel = viewModel,
        authUiState = authUiState
    ) { passwordFocusRequester.requestFocus() }

    PasswordInput(
        modifier = Modifier.focusRequester(passwordFocusRequester),
        passwordState = passwordState,
    ) { passwordFocusRequester.freeFocus() }

    AppButton(
        buttonText = stringResource(
            id = if (isCreateAccount) R.string.create_account else R.string.login
        )
    ) {
        keyboardController?.hide()

        if (isCreateAccount) {
            viewModel.createNewUser { onAuthSuccess() }
        } else {
            viewModel.signUser { onAuthSuccess() }
        }
    }
}