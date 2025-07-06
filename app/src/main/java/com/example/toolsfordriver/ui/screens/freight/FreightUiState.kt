package com.example.toolsfordriver.ui.screens.freight

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.toolsfordriver.data.model.Freight

data class FreightUiState(
    val freights: MutableState<List<Freight>> = mutableStateOf(emptyList()),
    val currentFreight: Freight? = null,
    val currentFreightBeforeChange: Freight? = null,
    val isNewFreight: Boolean = false,
    val swipedItemId: String = "",
    val showFreightContent: Boolean = false,
    val freightToDelete: Freight? = null,
    val showDeleteImagePopup: Boolean = false,
    val showDeleteItemPopup: Boolean = false,
    val showDeleteItemConfDialog: Boolean = false,
    val showCamera: Boolean = false,
    val showZoomableImageDialog: Boolean = false,
    val zoomableImageUri: Uri? = null,
    val addedUriList: List<String> = listOf(),
    val imageUriToDeleteList: List<Uri> = listOf()
)