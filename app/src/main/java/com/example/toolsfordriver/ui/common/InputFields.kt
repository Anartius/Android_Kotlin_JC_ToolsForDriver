package com.example.toolsfordriver.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.SuffixTransformation
import com.example.toolsfordriver.common.isDotOrComma

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    textValueState: MutableState<String>,
    label: String = "",
    placeholder: @Composable (() -> Unit)? = {},
    isOutlined: Boolean = true,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    textVisibility: Boolean = true,
    inputTextAlign: TextAlign = TextAlign.Start,
    trailingIcon: ImageVector = Icons.Filled.Edit,
    trailingIconDescription: String = stringResource(R.string.edit),
    trailingIconVisibility: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onChangeVisibility: () -> Unit = {},
    onAction: () -> Unit = {}
) {
    val colorBlue = colorResource(id = R.color.light_blue)
    val customTextSelectionColors = TextSelectionColors(
        handleColor = colorBlue,
        backgroundColor = colorBlue.copy(alpha = 0.7f)
    )

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = textValueState.value,
                selection = TextRange(textValueState.value.length)
            )
        )
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors
    ) {
        if (isOutlined) {
            OutlinedTextField(
                value = textFieldValueState,
                onValueChange = {
                    textFieldValueState = TextFieldValue(
                        text = it.text,
                        selection = it.selection
                    )
                    textValueState.value = textFieldValueState.text
                },
                label = { Text(text = label) },
                singleLine = isSingleLine,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = keyboardOptions.imeAction
                ),
                visualTransformation = visualTransformation,
                trailingIcon = if (trailingIconVisibility) {
                    { TextVisibility(textVisibility = textVisibility) { onChangeVisibility() } }
                } else null,
                keyboardActions = KeyboardActions(
                    onDone = { onAction.invoke() },
                    onNext = { onAction.invoke() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorBlue,
                    focusedLabelColor = colorBlue,
                    cursorColor = colorBlue
                )
            )
        } else {
            TextField(
                value = textFieldValueState,
                onValueChange = {
                    textFieldValueState = TextFieldValue(
                        text = it.text,
                        selection = it.selection
                    )
                    textValueState.value = textFieldValueState.text
                },
                modifier = modifier,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = colorBlue,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = inputTextAlign
                ),
                maxLines = maxLines,
                placeholder = placeholder,
                singleLine = false,
                trailingIcon = if (trailingIconVisibility) {
                    {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = trailingIconDescription,
                            tint = colorResource(id = R.color.light_blue),
                            modifier = Modifier
                                .padding(0.dp)
                                .scale(0.8f)
                        )
                    }
                } else null,
                keyboardOptions = keyboardOptions.copy(
                    keyboardType = keyboardType
                ),
                keyboardActions = KeyboardActions (
                    onDone = { onAction.invoke() },
                    onNext = { onAction.invoke() }
                )
            )
        }
    }
}

@Composable
fun DigitInputField(
    modifier: Modifier,
    textValue: MutableState<String>,
    placeholder: String,
    suffix: String,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Done,
    onDone: () -> Unit = {}
) {
    val colorBlue = colorResource(id = R.color.light_blue)
    val customTextSelectionColors = TextSelectionColors(
        handleColor = colorBlue,
        backgroundColor = colorBlue.copy(alpha = 0.7f)
    )
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = textValue.value,
                selection = TextRange(textValue.value.length)
            )
        )
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors
    ) {
        TextField(
            value = textFieldValueState,
            onValueChange = { input ->
                textFieldValueState = TextFieldValue(
                    text = if (
                        input.text.isNotEmpty() && input.text.first().isDotOrComma() ||
                        input.text.isNotEmpty() && input.text.count { it.isDotOrComma() } > 1
                    ) {
                        input.text.dropLast(1)
                            .filter { symbol -> symbol.isDigit() || symbol.isDotOrComma() }
                    } else if (
                        input.text.length > 1 && input.text.count { it.isDotOrComma() } == 0 &&
                        input.text.first() == '0'
                    ){
                        input.text.drop(1)
                            .filter { symbol -> symbol.isDigit() || symbol.isDotOrComma() }
                    } else {
                        input.text.filter { symbol -> symbol.isDigit() || symbol.isDotOrComma() }
                    },
                    selection = input.selection
                )

                textValue.value = textFieldValueState.text
            },
            modifier = modifier,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
            placeholder = { Text(text = placeholder, color = colorResource(id = R.color.gray)) },
            maxLines = maxLines,
            singleLine = false,
            keyboardOptions = keyboardOptions.copy(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDone.invoke() }
            ),
            visualTransformation = if (textFieldValueState.text.isEmpty()) {
                VisualTransformation.None
            } else {
                SuffixTransformation(suffix)
            }
        )
    }
}