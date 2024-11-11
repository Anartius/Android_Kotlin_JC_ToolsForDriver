package com.example.toolsfordriver.ui.screens.auth.content

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.InputField
import com.example.toolsfordriver.ui.screens.auth.AuthScreenViewModel
import com.example.toolsfordriver.ui.screens.auth.AuthUiState

@Composable
fun EmailInput(
    viewModel: AuthScreenViewModel,
    authUiState: AuthUiState,
    onAction: () -> Unit = {}
) {
    val email = authUiState.email
    val emailState = remember { mutableStateOf(email) }

    LaunchedEffect(emailState.value) { viewModel.updateEmail(emailState.value) }

    InputField(
        textValueState = emailState,
        label = stringResource(id = R.string.email),
        keyboardType = KeyboardType.Email,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    ) { onAction() }
}