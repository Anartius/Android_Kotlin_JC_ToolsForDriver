package com.example.toolsfordriver.ui.common.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(
            width = 0.5.dp,
            color = if (isEnabled) {
                colorResource(id = R.color.light_blue).copy(0.6f)
            } else colorResource(id = R.color.gray)
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.light_blue),
            disabledContainerColor = Color.Transparent
        )
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleMedium
        )
    }
}