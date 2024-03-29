package com.example.toolsfordriver.ui.content.freight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R

@Composable
fun ItemsTitleRow(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector = Icons.Filled.Edit,
    showIcon: Boolean = false,
    iconDescription: String = stringResource(id = R.string.icon),
    onIconClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.light_blue)
        )
        if (showIcon) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                modifier = modifier
                    .padding(0.dp)
                    .clickable { onIconClick.invoke() },
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}