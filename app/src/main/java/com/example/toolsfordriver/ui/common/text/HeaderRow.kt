package com.example.toolsfordriver.ui.common.text

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toolsfordriver.R

@Composable
fun HeaderRow(
    text: String,
    onClick: () -> Unit = {}
) {
    HorizontalDivider(
        thickness = 16.dp,
        color = MaterialTheme.colorScheme.background
    )

    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.light_blue),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    HorizontalDivider(
        thickness = 8.dp,
        color = MaterialTheme.colorScheme.background
    )
}