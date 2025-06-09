package com.example.toolsfordriver.ui.common.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R

@Composable
fun FAB(
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