package com.example.toolsfordriver.ui.common.dialogs

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.toolsfordriver.ui.common.ZoomableImage

@Composable
fun ZoomableImageDialog(
    imageUri: Uri?,
    onDismiss: () -> Unit
) {
    if (imageUri != null) {

        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                ZoomableImage(
                    model = imageUri,
                    contentDescription = "zoomable fullscreen image"
                )
            }
        }
    }
}