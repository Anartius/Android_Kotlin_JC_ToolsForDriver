package com.example.toolsfordriver.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.rotateBitmap
import com.example.toolsfordriver.data.model.User

@Composable
fun Camera(
    user: User?,
    onDone: (Bitmap?) -> Unit
) {
    BackHandler { onDone(null) }

    val context = LocalContext.current

    val camController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    var isBackCamera by remember {
        mutableStateOf(camController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
    }

    var isFlashOn by remember {
        mutableStateOf(camController.imageCaptureFlashMode == ImageCapture.FLASH_MODE_ON)
    }

    LaunchedEffect(key1 = isFlashOn, key2 = isBackCamera) {
        if (isBackCamera) {
            camController.imageCaptureFlashMode = if (isFlashOn) {
                ImageCapture.FLASH_MODE_ON
            } else ImageCapture.FLASH_MODE_OFF
        }

        camController.cameraSelector = if (isBackCamera) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else CameraSelector.DEFAULT_FRONT_CAMERA
    }

    Box (modifier = Modifier.fillMaxSize()) {
        if (user != null) {

            CameraPreview(
                cameraController = camController,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { if (isBackCamera) isFlashOn = !isFlashOn }
                ) {
                    Icon(
                        imageVector = if (isFlashOn) {
                            Icons.Filled.FlashOn
                        } else Icons.Filled.FlashOff,
                        contentDescription = "Flash mode",
                        tint = if (isFlashOn) {
                            colorResource(id = R.color.light_blue)
                        } else Color.LightGray
                    )
                }

                IconButton(
                    onClick = { takePhoto(camController,context) { bitmap -> onDone(bitmap) } }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhotoCamera,
                        contentDescription = "Take a photo"
                    )
                }
                IconButton(
                    onClick = {
                        if (isBackCamera) {
                            isBackCamera = false
                            isFlashOn = false
                        } else isBackCamera = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cameraswitch,
                        contentDescription = "Switch camera"
                    )
                }
            }
        }
    }
}

private fun takePhoto(
    camController: LifecycleCameraController,
    context: Context,
    onPhotoTaken: (Bitmap) -> Unit
) {
    camController.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val rotationDegrees = image.imageInfo.rotationDegrees
                onPhotoTaken(image.toBitmap().rotateBitmap(rotationDegrees))
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Error taking a photo", exception)
            }
        }
    )
}

@Composable
fun CameraPreview(
    cameraController: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                this.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}
