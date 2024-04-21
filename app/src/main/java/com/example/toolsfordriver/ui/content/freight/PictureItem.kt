package com.example.toolsfordriver.ui.content.freight

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.toolsfordriver.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PictureItem(
    uri: String,
    context: Context,
    showZoomableImageDialog: MutableState<Boolean>,
    showDeleteImagePopup: MutableState<Boolean>,
    dialogImageUri: MutableState<Uri?>,
    imageUriToDelete: MutableState<Uri?>
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 12.dp)
            .height(130.dp)
            .fillMaxWidth()
            .combinedClickable(
                enabled = true,
                onClick = {
                    dialogImageUri.value = uri.toUri()
                    showZoomableImageDialog.value = true
                },
                onLongClick = {
                    imageUriToDelete.value = uri.toUri()
                    showDeleteImagePopup.value = true
                }
            ),
        color = Color.Transparent,
        tonalElevation = 10.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(uri)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = stringResource(id = R.string.attached_image),
            modifier = Modifier.clip(
                RoundedCornerShape(corner = CornerSize(5.dp))
            ),
            contentScale = ContentScale.FillWidth
        )
    }
}