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
import com.example.toolsfordriver.common.LocaleManager
import com.example.toolsfordriver.ui.common.Camera
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.dialogs.ZoomableImageDialog
import java.util.Locale

@Composable
fun MyProfileScreen(
    onNavIconClicked: () -> Unit,
    onSignOutIconClicked: () -> Unit
) {
    val viewModel: MyProfileViewModel = hiltViewModel()
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val currentUser = uiState.user
    val showCamera = uiState.showCamera
    val showZoomableImageDialog = uiState.showZoomableImageDialog

    LaunchedEffect(key1 = users) {
        if (users.isNotEmpty()) { viewModel.updateCurrentUser(users.first()) }
    }

    if (currentUser != null) {
        val context = LocalContext.current

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
                                stringResource(id = R.string.sign_out)
                            ) { viewModel.showSignOutDialog(true) }
                        )
                    )
                }
            ) { paddingValues ->

                val showSelectLocaleDialog = uiState.showSelectLocaleDialog
                val showSignOutDialog = uiState.showSignOutDialog

                Surface(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    val localeOptions = mapOf(
                        Locale("en") to stringResource(R.string.english),
                        Locale("ru") to stringResource(R.string.russian)
                    )

                    MyProfileLazyColumn(localeOptions = localeOptions)

                    if (showSelectLocaleDialog) {
                        val locale = LocaleManager.getSavedLocale(context)

                        SelectLocaleDialog(
                            localeOptions = localeOptions,
                            locale = locale
                        ) { selectedLocale ->
                            viewModel.showSelectLocaleDialog(false)
                            viewModel.updateLocale(selectedLocale, context)
                        }
                    }

                    if (showSignOutDialog) {
                        SignOutDialog {
                            viewModel.showSignOutDialog(false)
                            viewModel.onSignOutClick { onSignOutIconClicked() }
                        }
                    }
                }
            }
        }
    }
}