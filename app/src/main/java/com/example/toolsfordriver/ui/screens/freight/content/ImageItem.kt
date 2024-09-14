package com.example.toolsfordriver.ui.screens.freight.content

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.toolsfordriver.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageItem(
    uri: String,
    context: Context,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 12.dp)
            .height(130.dp)
            .fillMaxWidth()
            .combinedClickable(
                enabled = true,
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            ),
        color = Color.Transparent,
        tonalElevation = 10.dp
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(uri)
                .crossfade(true)
                .build(),
            error = {
                painterResource(id = R.drawable.ic_broken_image)
            },
            loading = {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(id = R.color.light_blue))
                }
            },
            contentDescription = stringResource(id = R.string.attached_image),
            modifier = Modifier.clip(
                RoundedCornerShape(corner = CornerSize(5.dp))
            ),
            contentScale = ContentScale.FillWidth
        )
    }
}