package com.example.toolsfordriver.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R

@Composable
fun AppButton(
    buttonText: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 50.dp, vertical = 0.dp),
    enabled: Boolean = true,
    load: Boolean = false,
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
        if (load) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium
            )
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