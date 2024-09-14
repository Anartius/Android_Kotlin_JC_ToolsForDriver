package com.example.toolsfordriver.ui.screens.myprofile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.Camera
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.dialogs.ZoomableImageDialog

@Composable
fun MyProfileScreen(
    onNavIconClicked: () -> Unit,
    onSignOutIconClicked: () -> Unit
) {
    val viewModel: MyProfileViewModel = hiltViewModel()
    val context = LocalContext.current
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser = uiState.value.user
    val showCamera = uiState.value.showCamera
    val showZoomableImageDialog = uiState.value.showZoomableImageDialog

    LaunchedEffect(key1 = users) {
        if (users.isNotEmpty()) { viewModel.updateCurrentUser(users.first()) }
    }

    if (currentUser != null) {
        if (showCamera) {
            Camera(user = currentUser) { bitmap ->
                viewModel.showCamera(false)
                viewModel.saveAvatarToCloud(
                    bitmap = bitmap,
                    user = currentUser,
                    context = context
                )
            }

        } else if (showZoomableImageDialog) {
            ZoomableImageDialog(imageUri = currentUser.avatarUri.toUri()) {
                viewModel.showAvatarImage(false)
            }

        } else {
            Scaffold(
                topBar = {
                    TFDAppBar(
                        title = stringResource(id = R.string.my_profile),
                        navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        onNavIconClicked = { onNavIconClicked() },
                        actions = listOf(
                            Triple(
                                Icons.AutoMirrored.Filled.Logout,
                                stringResource(id = R.string.log_out)
                            ) {
                                viewModel.onSignOutClick()
                                onSignOutIconClicked()
                            }
                        )
                    )
                }
            ) { paddingValues ->
                Surface(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    MyProfileLazyColumn(viewModel = viewModel)
                }
            }
        }
    }
}

