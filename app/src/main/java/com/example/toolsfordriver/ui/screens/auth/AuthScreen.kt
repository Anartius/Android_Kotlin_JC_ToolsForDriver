package com.example.toolsfordriver.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.AppButton
import com.example.toolsfordriver.ui.common.InputField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val viewModel: AuthScreenViewModel = hiltViewModel()
    val isNewAccount = viewModel.authUiState.collectAsStateWithLifecycle().value.isNewAccount
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.snackbarMessages) {
        viewModel.snackbarMessages.collect { snackbarMessage ->
            val job = launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage.asString(context),
                    duration = SnackbarDuration.Indefinite
                )
            }
            delay(5000)
            job.cancel()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        }
    ) { paddingValues ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp
                ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isNewAccount) {
                    UserForm(viewModel = viewModel) { onAuthSuccess() }
                } else {
                    UserForm(viewModel = viewModel) { onAuthSuccess() }
                }

                Button(
                    onClick = { viewModel.invertIsNewAccountValue() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 30.dp),
                    shape = RoundedCornerShape(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val text = stringResource(
                            id = if (isNewAccount) R.string.have_account else R.string.new_account
                        ) + "?"

                        val actionText = stringResource(
                            id = if (isNewAccount) R.string.login else R.string.sign_up
                        )

                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = actionText,
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.light_blue)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserForm(
    viewModel: AuthScreenViewModel,
    onAuthSuccess: () -> Unit
) {
    val passwordFocusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    val authUiState = viewModel.authUiState.collectAsStateWithLifecycle().value
    val isCreateAccount = authUiState.isNewAccount

    Spacer(modifier = Modifier.height(150.dp))

    EmailInput(
        viewModel = viewModel,
        authUiState = authUiState
    ) { passwordFocusRequester.requestFocus() }

    PasswordInput(
        modifier = Modifier.focusRequester(passwordFocusRequester),
        viewModel = viewModel,
        authUiState = authUiState
    ) { passwordFocusRequester.freeFocus() }

    AppButton(
        buttonText = stringResource(
            id = if (isCreateAccount) R.string.create_account else R.string.login
        )
    ) {
        keyboardController?.hide()

        if (isCreateAccount) {
            viewModel.onCreateNewUserClick { onAuthSuccess() }
        } else {
            viewModel.onSignUserClick { onAuthSuccess() }
        }
    }
}

@Composable
fun EmailInput(
    viewModel: AuthScreenViewModel,
    authUiState: AuthUiState,
    onAction: () -> Unit = {}
) {
    val email = authUiState.email
    val emailState = remember { mutableStateOf(email) }

    LaunchedEffect(emailState.value) { viewModel.onEmailChange(emailState.value) }

    InputField(
        textValueState = emailState,
        label = stringResource(id = R.string.email),
        keyboardType = KeyboardType.Email,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    ) { onAction() }
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    viewModel: AuthScreenViewModel,
    authUiState: AuthUiState,
    onAction: () -> Unit = {}
) {
    val password = authUiState.password
    val passwordState = remember { mutableStateOf(password) }

    LaunchedEffect(passwordState.value) { viewModel.onPasswordChange(passwordState.value) }

    val passwordVisibility = authUiState.passwordVisibility
    val visualTransformation = if (passwordVisibility) {
        VisualTransformation.None
    } else PasswordVisualTransformation()

    InputField(
        modifier = modifier,
        textValueState = passwordState,
        label = stringResource(id = R.string.password),
        textVisibility = passwordVisibility,
        trailingIconVisibility = true,
        keyboardType = KeyboardType.Password,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = visualTransformation,
        onChangeVisibility = { viewModel.invertPasswordVisibilityValue() }
    ) { onAction() }
}