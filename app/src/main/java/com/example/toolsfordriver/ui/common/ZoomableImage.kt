package com.example.toolsfordriver.ui.common

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.toolsfordriver.R

@Composable
fun ZoomableImage(
    model: Any,
    contentDescription: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    var offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }

    SubcomposeAsyncImage(
        model = model,
        loading = {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(id = R.color.light_blue))
            }
        },
        error = { painterResource(id = R.drawable.ic_broken_image) },
        contentDescription = contentDescription,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)

                    val extraWidth = (scale - 1) * screenWidth
                    val extraHeight = (scale - 1) * screenHeight
                    val maxX = extraWidth / 2
                    val maxY = extraHeight / 2

                    offset = if (scale == 1f) {
                        Offset.Zero
                    } else {
                        Offset(
                            x = (offset.x + scale * 0.7f * pan.x).coerceIn(-maxX..maxY),
                            y = (offset.y + scale * 0.7f * pan.y).coerceIn(-maxY..maxY)
                        )
                    }
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}