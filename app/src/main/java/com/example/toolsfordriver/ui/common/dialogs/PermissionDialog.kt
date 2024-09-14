package com.example.toolsfordriver.ui.common.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R

@Composable
fun PermissionDialog(onRequestPermission: () -> Unit) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        showDialog = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 16.dp, 0.dp)
                ) {
                    Text(text = stringResource(R.string.request_permission))
                }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text(text = stringResource(R.string.notification_permission_title)) },
            text = { Text(text = stringResource(R.string.notification_permission_description)) }
        )
    }
}

@Composable
fun RationaleDialog(onConfirm: () -> Unit) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onConfirm()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 16.dp, 0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.allow),
                        color = colorResource(id = R.color.light_blue)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 16.dp, 0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.deny),
                        color = colorResource(id = R.color.light_blue)
                    )
                }
            },
            modifier = Modifier.wrapContentSize(),
            title = {
                Text(
                    text = stringResource(R.string.notification_permission_title),
                    color = colorResource(id = R.color.light_blue)
                )
            },
            text = { Text(text = stringResource(R.string.notification_permission_description)) }
        )
    }
}