package com.example.toolsfordriver.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.toolsfordriver.R

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    msg: String,
    textColor: Color = Color.White,
    containerColor: Color = colorResource(R.color.dark_gray)
) {
    Snackbar(containerColor = containerColor) {
        Row(modifier = modifier.fillMaxWidth()) {
            Text(
                text = msg,
                color = textColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}