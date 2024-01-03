package com.example.toolsfordriver.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.InputField
import com.example.toolsfordriver.navigation.TFDScreens

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthScreenViewModel = viewModel()
) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showLoginForm.value) {
                UserForm(loading = false, isCreateAccount = false) { email, password ->
                    viewModel.signUserWithEmailAndPassword(email, password, context) {
                        navController.navigate(TFDScreens.HomeScreen.name)
                    }
                }
            } else {
                UserForm(loading = false, isCreateAccount = true) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(TFDScreens.HomeScreen.name)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) "New User?" else "Have account?"
                val actionText = if (showLoginForm.value) "Sign Up" else "Login "

                Text(text = text)

                Text(
                    text = actionText,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable { showLoginForm.value = !showLoginForm.value },
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.light_blue)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.isNotEmpty()
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
                text = if (!isCreateAccount) {
                    stringResource(R.string.create_account)
                } else "",
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 50.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    EmailInput(
        emailState = email,
        enabled = !loading,
        onAction = KeyboardActions { passwordFocusRequest.requestFocus() }
    )

    PasswordInput(
        modifier = Modifier.focusRequester(passwordFocusRequest),
        passwordState = password,
        labelId = "Password",
        enabled = !loading,
        passwordVisibility = passwordVisibility,
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onDone(email.value.trim(), password.value.trim())
        }
    )

    SubmitButton(
        validInputs = valid,
        loading = loading,
        textId = if (isCreateAccount) "Create Account" else "Login"
    ) {
        onDone(email.value.trim(), password.value.trim())
        keyboardController?.hide()
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        onAction = onAction
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val trailingIconVisibility = remember {
        mutableStateOf(true)
    }

    val visualTransformation = if (passwordVisibility.value) {
        VisualTransformation.None
    } else PasswordVisualTransformation()

    InputField(
        modifier = modifier,
        valueState = passwordState,
        labelId = labelId,
        enabled = enabled,
        textVisibility = passwordVisibility,
        trailingIconVisibility = trailingIconVisibility,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        visualTransformation = visualTransformation,
        onAction = onAction
    )
}

@Composable
fun SubmitButton(
    validInputs: Boolean,
    loading: Boolean,
    textId: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(text = textId, modifier = Modifier.padding(5.dp))
        }
    }
}