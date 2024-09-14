package com.example.toolsfordriver.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.toolsfordriver.ui.common.dialogs.PermissionDialog
import com.example.toolsfordriver.ui.common.dialogs.RationaleDialog
import com.example.toolsfordriver.navigation.TFDNavigation
import com.example.toolsfordriver.ui.utils.TFDContentType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun TFDApp(windowSize: WindowWidthSizeClass) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val contentType: TFDContentType

        when (windowSize) {
            WindowWidthSizeClass.Compact -> {
                contentType = TFDContentType.LIST_ONLY
                TFDNavigation()
            }

            WindowWidthSizeClass.Medium -> {
                contentType = TFDContentType.LIST_ONLY
                TFDNavigation()
            }

            WindowWidthSizeClass.Expanded -> {
                contentType = TFDContentType.LIST_AND_DETAIL
                TFDNavigation()
            }

            else -> {
                contentType = TFDContentType.LIST_ONLY
                TFDNavigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}
