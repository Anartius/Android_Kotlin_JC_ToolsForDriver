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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.components.AppButton
import com.example.toolsfordriver.ui.components.InputField
import com.example.toolsfordriver.utils.isValidEmail

@Composable
fun AuthScreen(
    viewModel: AuthScreenViewModel = viewModel(),
    onAuthSuccess: () -> Unit
) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showLoginForm.value) {
                UserForm(load = false, isCreateAccount = false) { email, password ->
                    viewModel.signUserWithEmailAndPassword(email, password, context) {
                        onAuthSuccess()
                    }
                }
            } else {
                UserForm(load = false, isCreateAccount = true) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        onAuthSuccess()
                    }
                }
            }

            Button(
                onClick = { showLoginForm.value = !showLoginForm.value },
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
                        id = if (showLoginForm.value) R.string.new_user else R.string.have_account
                    ) + "?"

                    val actionText = stringResource(
                        id = if (showLoginForm.value) R.string.sign_up else R.string.login
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
    load: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isValidEmail() &&
                password.value.isNotEmpty() &&
                password.value.length > 5
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
        emailState = email,
        enabled = !load
    ) {
        passwordFocusRequester.requestFocus()
    }

    PasswordInput(
        modifier = Modifier.focusRequester(passwordFocusRequester),
        passwordState = password,
        labelId = stringResource(id = R.string.password),
        enabled = !load,
        passwordVisibility = passwordVisibility
    ) {
        if (valid) onDone(email.value.trim(), password.value.trim())
        passwordFocusRequester.freeFocus()
    }

    AppButton(
        buttonText = stringResource(
            id = if (isCreateAccount) R.string.create_account else R.string.login
        ),
        enabled = !load && valid
    ) {
        onDone(email.value.trim(), password.value.trim())
        keyboardController?.hide()
    }

}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = stringResource(id = R.string.email),
    enabled: Boolean = true,
    onAction: () -> Unit = {}
) {
    InputField(
        textValueState = emailState,
        label = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        onAction = onAction
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String = stringResource(id = R.string.password),
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    onAction: () -> Unit = {}
) {
    val trailingIconVisibility = remember {
        mutableStateOf(true)
    }

    val visualTransformation = if (passwordVisibility.value) {
        VisualTransformation.None
    } else PasswordVisualTransformation()

    InputField(
        modifier = modifier,
        textValueState = passwordState,
        label = labelId,
        enabled = enabled,
        textVisibility = passwordVisibility,
        trailingIconVisibility = trailingIconVisibility,
        keyboardType = KeyboardType.Password,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = visualTransformation,
        onAction = onAction
    )
}