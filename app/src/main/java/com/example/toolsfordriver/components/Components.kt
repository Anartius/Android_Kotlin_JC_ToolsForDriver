package com.example.toolsfordriver.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    textVisibility: MutableState<Boolean> = mutableStateOf(true),
    trailingIconVisibility: MutableState<Boolean> = mutableStateOf(false),
    keyboardType: KeyboardType = KeyboardType.Text,
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
        OutlinedTextField(
            value = valueState.value,
            onValueChange = { valueState.value = it },
            label = { Text(text = labelId) },
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
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 0.dp),
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