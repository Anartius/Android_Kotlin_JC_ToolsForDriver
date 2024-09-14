package com.example.toolsfordriver.ui.screens.freight

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.toolsfordriver.data.model.Freight
import com.example.toolsfordriver.data.model.User

data class FreightUiState(
    val user: User? = null,
    val freights: MutableState<List<Freight>> = mutableStateOf(emptyList()),
    val currentFreight: Freight? = null,
    val isNewFreight: Boolean = false,
    val showFreightContent: Boolean = false,
    val freightToDelete: Freight? = null,
    val showDeletePopup: Boolean = false,
    val showDeleteImagePopup: Boolean = false,
    val showDeleteItemPopup: Boolean = false,
    val showCamera: Boolean = false,
    val showZoomableImageDialog: Boolean = false,
    val zoomableImageUri: Uri? = null,
    val addedUriList: List<String> = listOf(),
    val imageUriToDeleteList: List<Uri> = listOf()
)