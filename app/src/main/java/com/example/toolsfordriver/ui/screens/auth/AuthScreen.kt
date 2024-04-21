package com.example.toolsfordriver.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.AuthUiState
import com.example.toolsfordriver.ui.components.AppButton
import com.example.toolsfordriver.ui.components.InputField
import com.example.toolsfordriver.utils.isValidEmail

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val viewModel: AuthScreenViewModel = viewModel()
    val isNewAccount = viewModel.authUiState.collectAsStateWithLifecycle().value.isNewAccount

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
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
                        id = if (isNewAccount) R.string.new_user else R.string.have_account
                    ) + "?"

                    val actionText = stringResource(
                        id = if (isNewAccount) R.string.sign_up else R.string.login
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

@Composable
fun UserForm(
    viewModel: AuthScreenViewModel,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val passwordFocusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    val authUiState = viewModel.authUiState.collectAsStateWithLifecycle().value
    val email = authUiState.email
    val password = authUiState.password
    val isCreateAccount = authUiState.isNewAccount

    val inputIsValid = remember(email, password) {
        email.trim().isValidEmail() && password.isNotEmpty() && password.length > 5
    }

    Column(
        modifier = Modifier
            .wrapContentHeight(align = Alignment.CenterVertically)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val headerHintPlaceholderHeight = with(LocalDensity.current) {
            LocalTextStyle.current.lineHeight.toDp() * 3 + 80.dp
        }

        Column(
            modifier = Modifier.height(headerHintPlaceholderHeight),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.create_account_prompt),
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 50.dp),
                textAlign = TextAlign.Center
            )
        }
    }

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
        ),
        enabled = inputIsValid
    ) {
        keyboardController?.hide()

        if (isCreateAccount) {
            viewModel.createNewUser(context) { onDone() }
        } else {
            viewModel.signUser(context) { onDone() }
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

    LaunchedEffect(emailState.value) { viewModel.updateEmail(emailState.value) }

    InputField(
        textValueState = emailState,
        label = stringResource(id = R.string.email),
        enabled = true,
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

    LaunchedEffect(passwordState.value) { viewModel.updatePassword(passwordState.value) }

    val passwordVisibility = authUiState.passwordVisibility
    val visualTransformation = if (passwordVisibility) {
        VisualTransformation.None
    } else PasswordVisualTransformation()

    InputField(
        modifier = modifier,
        textValueState = passwordState,
        label = stringResource(id = R.string.password),
        enabled = true,
        textVisibility = passwordVisibility,
        trailingIconVisibility = true,
        keyboardType = KeyboardType.Password,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = visualTransformation,
        onChangeVisibility = { viewModel.invertPasswordVisibilityValue() }
    ) { onAction() }
}