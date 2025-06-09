package com.example.toolsfordriver.ui.common.textfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.toolsfordriver.R

@Composable
fun CenteredTextRow (
    text: String,
    fontSize: TextUnit = 18.sp,
    textColor: Int = R.color.light_blue,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = textColor)
        )
    }
}