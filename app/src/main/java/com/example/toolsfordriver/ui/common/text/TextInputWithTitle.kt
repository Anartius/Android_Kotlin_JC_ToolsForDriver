package com.example.toolsfordriver.ui.common.text

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.ui.common.InputField

@Composable
fun TextInputWithTitle(
    title: String,
    valueState: MutableState<String>,
    placeHolder: String,
    onFocusChanged: () -> Unit = {},
    onDone: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TitleRowWithIcons(
        text = title,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    InputField(
        textValueState = valueState,
        placeholder = { Text(text = placeHolder) },
        isOutlined = false,
        modifier = Modifier.padding(horizontal = 8.dp).onFocusChanged { onFocusChanged() },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    ) {
        onDone()
        focusManager.clearFocus()
        keyboardController?.hide()
    }
}