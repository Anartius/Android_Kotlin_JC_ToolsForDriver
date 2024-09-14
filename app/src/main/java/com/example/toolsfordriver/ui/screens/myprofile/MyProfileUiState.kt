package com.example.toolsfordriver.ui.screens.myprofile

import android.net.Uri
import com.example.toolsfordriver.data.model.User

data class MyProfileUiState(
    val user: User? = null,
    val showCamera: Boolean = false,
    val temporaryImageUri: Uri? = null,
    val showZoomableImageDialog: Boolean = false
)
