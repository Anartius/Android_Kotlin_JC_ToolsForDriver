package com.example.toolsfordriver.ui.common.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R

@Composable
fun TitleRowWithIcons(
    modifier: Modifier,
    text: String,
    primIcon: ImageVector = Icons.Filled.Edit,
    showPrimIcon: Boolean = false,
    primIconDescription: String = stringResource(id = R.string.icon),
    primIconSize: Dp = 24.dp,
    secIcon: ImageVector = Icons.Filled.QuestionMark,
    showSecIcon: Boolean = false,
    secIconDescription: String = stringResource(id = R.string.icon),
    secIconSize: Dp = 24.dp,
    onSecIconClick: () -> Unit = {},
    onPrimIconClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.light_blue)
        )

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSecIcon) {
                Icon(
                    imageVector = secIcon,
                    contentDescription = secIconDescription,
                    modifier = Modifier.padding(end = 8.dp).size(secIconSize)
                        .clickable { onSecIconClick() },
                    tint = colorResource(id = R.color.light_blue)
                )
            }

            if (showPrimIcon) {
                Icon(
                    imageVector = primIcon,
                    contentDescription = primIconDescription,
                    modifier = Modifier.size(primIconSize).clickable { onPrimIconClick() },
                    tint = colorResource(id = R.color.light_blue)
                )
            }
        }
    }
}