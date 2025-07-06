package com.example.toolsfordriver.ui.screens.freight.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.ui.common.TextRow
import java.time.ZonedDateTime

@Composable
fun LoadUnloadRow(
    item: Pair<String, String>,
    location: String,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val dateTime = ZonedDateTime.parse(item.first)

    TextRow(
        valueDescription = location.replace("#", ", "),
        value = "${dateAsString(dateTime)}  ${timeAsString(dateTime)}",
        firstTextColor = MaterialTheme.colorScheme.onBackground,
        clickable = true,
        onLongClick = onLongClick,
        onClick = onClick
    )
}