package com.example.toolsfordriver.ui.common

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ActionIcon(
    icon: ImageVector,
    iconDescription: String? = null,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            tint = tint
        )
    }
}