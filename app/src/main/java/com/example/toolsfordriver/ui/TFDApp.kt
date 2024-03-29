package com.example.toolsfordriver.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.toolsfordriver.navigation.TFDNavigation
import com.example.toolsfordriver.ui.utils.TFDContentType

@Composable
fun TFDApp(windowSize: WindowWidthSizeClass) {
    val contentType: TFDContentType

    when(windowSize) {
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