package com.example.toolsfordriver.ui.common.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.isUnspecified

@Composable
fun AutoResizeableText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    var shouldDraw by remember { mutableStateOf(false) }
    var resizedTextStyle by remember { mutableStateOf(style) }
    val defaultFontSize = MaterialTheme.typography.bodyLarge.fontSize

    Text(
        text = text,
        modifier = modifier.drawWithContent { if (shouldDraw) drawContent() },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.9
                )
            } else shouldDraw = true
        },
        textAlign = textAlign
    )
}