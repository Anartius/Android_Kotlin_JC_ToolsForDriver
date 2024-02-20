package com.example.toolsfordriver.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.toolsfordriver.R

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
    valueDescription: String,
    value: String,
    clickable: Boolean = false,
    showIcon: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row (
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickable(
                enabled = clickable,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = valueDescription,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(text = value, modifier = Modifier.padding(end =  5.dp))

        if (showIcon) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "select",
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}




//soc-op.6120.1.40.2024.ws