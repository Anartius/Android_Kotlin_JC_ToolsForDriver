package com.example.toolsfordriver.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.ui.screens.freight.content.ItemTitleRow

@Composable
fun DigitalInputWithTitle(
    title: String,
    valueState: MutableState<String>,
    placeholder: String = "",
    suffix: String = "",
    onFocusChanged: () -> Unit,
    onDone: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    ItemTitleRow(
        title = title,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(vertical = 0.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DigitInputField(
            modifier = Modifier.onFocusChanged { onFocusChanged() },
            textValue = valueState,
            placeholder = placeholder,
            suffix = suffix,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ) {
            onDone()
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
}