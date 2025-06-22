package com.example.toolsfordriver.ui.common.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.InputField

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    onAction: () -> Unit = {}
) {
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

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
        onChangeVisibility = { passwordVisibility = !passwordVisibility }
    ) { onAction() }
}