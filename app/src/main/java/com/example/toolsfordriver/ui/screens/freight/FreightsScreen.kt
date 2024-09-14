package com.example.toolsfordriver.ui.screens.freight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.ui.common.Camera
import com.example.toolsfordriver.ui.common.dialogs.ZoomableImageDialog
import com.example.toolsfordriver.ui.screens.freight.content.FreightContent
import com.example.toolsfordriver.ui.screens.freight.content.FreightListContent

@Composable
fun FreightsScreen(onNavIconClicked: () -> Unit) {
    val context = LocalContext.current
    val viewModel: FreightViewModel = hiltViewModel()

    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val currentUser = uiState.user
    val showCamera = uiState.showCamera
    val showFreightContent = uiState.showFreightContent
    val showZoomableImage = uiState.showZoomableImageDialog
    val zoomableImageUri = uiState.zoomableImageUri

    LaunchedEffect(key1 = users) {
        if (users.isNotEmpty()) { viewModel.updateCurrentUser(users.first()) }
    }

    if (currentUser != null) {
        if (showCamera) {
            Camera(user = currentUser) { bitmap ->
                viewModel.showCamera(false)
                viewModel.addImageToCurrentFreight(bitmap = bitmap, context = context)
            }
        } else if (showZoomableImage) {
            ZoomableImageDialog(imageUri = zoomableImageUri) {
                viewModel.showZoomableImage(false)
                viewModel.showFreightContent(true)
            }
        } else if (showFreightContent) {
            FreightContent(viewModel = viewModel) {
                viewModel.showFreightContent(false)
                viewModel.clearCacheDirectory(context)
                viewModel.clearUnusedImages()
            }

        } else {
            FreightListContent(viewModel = viewModel) { onNavIconClicked() }
        }
    }
}