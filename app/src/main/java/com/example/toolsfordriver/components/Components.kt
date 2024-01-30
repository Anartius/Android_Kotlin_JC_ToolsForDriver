package com.example.toolsfordriver.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.toolsfordriver.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TFDAppBar(
    title: String,
    navIcon: ImageVector? = null,
    navIconDescription: String = "",
    actionIcon: ImageVector? = null,
    actionIconDescription: String = "",
    onNavIconClicked:() -> Unit = {},
    onActionIconClicked:() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = colorResource(id = R.color.light_blue),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
        },
        navigationIcon = {
            if (navIcon != null) {
                IconButton(
                    onClick = { onNavIconClicked.invoke() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colorResource(id = R.color.light_blue)
                    )
                ) {
                    Icon(imageVector = navIcon, contentDescription = navIconDescription)
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = { onActionIconClicked.invoke() }) {
                    Icon(imageVector = actionIcon, contentDescription = actionIconDescription)
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Transparent)
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    label: String,
    isOutlined: Boolean = true,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    textVisibility: MutableState<Boolean> = mutableStateOf(true),
    trailingIcon: ImageVector = Icons.Filled.Edit,
    trailingIconDescription: String = "edit",
    trailingIconVisibility: MutableState<Boolean> = mutableStateOf(false),
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val colorBlue = colorResource(id = R.color.light_blue)
    val customTextSelectionColors = TextSelectionColors(
        handleColor = colorBlue,
        backgroundColor = colorBlue.copy(alpha = 0.7f)
    )
    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors
    ) {
        if (isOutlined) {
            OutlinedTextField(
                value = valueState.value,
                onValueChange = { valueState.value = it },
                label = { Text(text = label) },
                singleLine = isSingleLine,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                visualTransformation = visualTransformation,
                trailingIcon = {
                    TextVisibility(
                        textVisibility = textVisibility,
                        iconVisibility = trailingIconVisibility
                    )
                },
                keyboardActions = onAction,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorBlue,
                    focusedLabelColor = colorBlue,
                    cursorColor = colorBlue
                )
            )
        } else {
            TextField(
                value = valueState.value,
                onValueChange = { valueState.value = it },
                modifier = modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(0.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = colorBlue
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                maxLines = maxLines,
                placeholder = {
                    Text(
                        text = label,
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                },
                singleLine = false,
                trailingIcon = {
                    InputFieldTrailingIcon(
                        icon = trailingIcon,
                        description = trailingIconDescription,
                        visibility = trailingIconVisibility
                    )
                },
                keyboardOptions = keyboardOptions.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = keyboardType
                ),
                keyboardActions = onAction
            )
        }
    }
}

@Composable
fun DigitInputField(
    modifier: Modifier = Modifier,
    textValue: MutableState<String>,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    focusRequester: FocusRequester,
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
    val keyboardController = LocalSoftwareKeyboardController.current
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
            onValueChange = {
                textFieldValueState = TextFieldValue(
                    text = it.text.filter { symbol -> symbol.isDigit() },
                    selection = it.selection
                )
            },
            modifier = modifier
                .focusable(true)
                .focusRequester(focusRequester)
                .onFocusChanged { keyboardController?.show() },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
            maxLines = maxLines,
            singleLine = false,

            keyboardOptions = keyboardOptions.copy(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    textValue.value = textFieldValueState.text
                    focusRequester.freeFocus()
                    onDone.invoke()
                }
            )
        )
    }
}


@Composable
fun InputFieldTrailingIcon(
    icon: ImageVector,
    description: String,
    visibility: MutableState<Boolean>
) {
    if (visibility.value) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = colorResource(id = R.color.light_blue),
            modifier = Modifier
                .padding(0.dp)
                .scale(0.8f)
        )
    }
}

@Composable
fun TextVisibility(
    textVisibility: MutableState<Boolean>,
    iconVisibility: MutableState<Boolean>
) {
    if (iconVisibility.value) {
        val visible = textVisibility.value

        IconButton(onClick = { textVisibility.value = !visible }) {
            Icon(
                imageVector = if (visible) {
                    Icons.Filled.Visibility
                } else Icons.Filled.VisibilityOff,
                contentDescription = "Hide password",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun AppButton(
    buttonText: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 50.dp, vertical = 0.dp),
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(
            width = 0.5.dp,
            color = if (enabled) {
                colorResource(id = R.color.light_blue).copy(0.6f)
            } else Color.Gray
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.light_blue),
            disabledContainerColor = Color.Transparent
        )
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(text = buttonText)
        }
    }
}


@Composable
fun FABContent(
    fabDescription: String,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = colorResource(id = R.color.light_blue),
        modifier = Modifier.size(55.dp),
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = fabDescription,
            tint = Color.White,
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
fun DeleteItemPopup(
    showDeletePopup: MutableState<Boolean>,
    itemName: String,
    onClick: () -> Unit
) {
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { showDeletePopup.value = false },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = false,
            dismissOnClickOutside = true,
            excludeFromSystemGesture = true
        )
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 20.dp)
                .clickable { onClick.invoke() },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.4f)
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            border = BorderStroke(
                width = 0.5.dp,
                color = colorResource(id = R.color.light_blue).copy(0.6f))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete $itemName",
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp)
                )
                Text(
                    text = "Delete $itemName",
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@Composable
fun TextRow(
    description: String,
    value: String,
    clickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(
                enabled = clickable,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = description,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = value)
    }
}




//soc-op.6120.1.40.2024.ws   obsl -