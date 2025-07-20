package com.example.toolsfordriver.data.model

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

class IconWithAction(
    val icon: ImageVector,
    val description: String,
    val tint: Color = Color.Unspecified,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit
)