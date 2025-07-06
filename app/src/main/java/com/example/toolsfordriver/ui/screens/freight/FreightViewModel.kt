package com.example.toolsfordriver.ui.screens.freight

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.saveBitmapToInternalStorage
import com.example.toolsfordriver.common.saveImageToInternalStorage
import com.example.toolsfordriver.data.model.Freight
import com.example.toolsfordriver.data.model.service.AccountService
import com.example.toolsfordriver.data.model.service.FirestoreService
import com.example.toolsfordriver.data.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreightViewModel @Inject constructor(
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val storageService: StorageService
) : ViewModel() {
    val userId = accountService.currentUserId
    private val snackbarChannel = Channel<UiText>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()
    private val _uiState = MutableStateFlow(FreightUiState())
    val uiState = _uiState.asStateFlow()
    val freights = firestoreService.freights
    val users = firestoreService.users

    fun addFreight(freight: Freight) {
        launchCatching {
            firestoreService.saveFreight(freight)
        }
    }

    fun addImageToCurrentFreight(
        uri: Uri? = null,
        bitmap: Bitmap? = null,
        context: Context
    ) = launchCatching {

        _uiState.value.currentFreight?.let { currentFreight ->
            if (uri != null || bitmap != null) {
                val images = currentFreight.imagesUriList.toMutableList()
                val addedUriList = _uiState.value.addedUriList.toMutableList()

                val temporaryUri = if (uri != null) {
                    saveImageToInternalStorage(uri, context)
                } else saveBitmapToInternalStorage(bitmap!!, context)

                if (temporaryUri.toString().isEmpty()) {
                    snackbarChannel.send(UiText.DynamicString("Image wasn't saved"))
                    return@launchCatching
                }

                val downloadUri = storageService.saveImage(
                    fileUri = temporaryUri,
                    isAvatar = false
                )
                if (downloadUri.isNotEmpty()) {
                    addedUriList.add(downloadUri)
                    images.add(downloadUri)
                    _uiState.value = _uiState.value.copy(
                        currentFreight = currentFreight.copy(imagesUriList = images),
                        addedUriList = addedUriList
                    )
                } else snackbarChannel.send(UiText.DynamicString("Image wasn't saved to cloud"))
            }
        }
    }

    fun addImageUriToDelete(uriList: List<Uri>) {
        _uiState.value = _uiState.value.copy(imageUriToDeleteList = uriList)
    }

    fun addFreightToDelete(freight: Freight) {
        _uiState.value = _uiState.value.copy(freightToDelete = freight)
    }

    fun clearAddedUriList() {
        _uiState.value = _uiState.value.copy(addedUriList = emptyList())
    }

    fun clearCacheDirectory(context: Context) {
        val cacheDir = context.cacheDir
        if (cacheDir?.exists() == true) {
            cacheDir.deleteRecursively()
        }
    }

    fun clearUnusedImages() = launchCatching{
        val addedUriList = _uiState.value.addedUriList.toMutableList()

        addedUriList.forEach { storageService.deleteFile(it.toUri()) }
    }

    fun deleteFreight(successMsg: String) {
        launchCatching {
            _uiState.value.freightToDelete?.let { firestoreService.deleteFreight(it.id) }
            _uiState.value = _uiState.value.copy(freightToDelete = null)
            showDeleteItemConfDialog(false)
            snackbarChannel.send(UiText.DynamicString(successMsg))
        }
    }

    fun deleteImagesAndUpdateFreight() = launchCatching {
        _uiState.value.imageUriToDeleteList.forEach { uri ->
            val result = storageService.deleteFile(uri)
            if (result) {
                val uriList = _uiState.value.imageUriToDeleteList.toMutableList()
                uriList.remove(uri)
                _uiState.value = _uiState.value.copy(imageUriToDeleteList = uriList)
            }

            _uiState.value.currentFreight?.let { freight -> updateFreight(freight) }
        }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler{ _, throwable ->
                viewModelScope.launch {
                    Log.e("Error", throwable.message.toString())
                    snackbarChannel.send(UiText.DynamicString(throwable.message.toString()))
                }
            },
            block = block
        )
    }

    fun setCurrentFreightAsNew(value: Boolean) {
        _uiState.update { it.copy(isNewFreight = value) }
    }

    fun setZoomableImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(zoomableImageUri = uri)
    }

    fun showCamera(value: Boolean) {
        _uiState.value = _uiState.value.copy(showCamera = value)
    }

    fun showDeleteImagePopup(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteImagePopup = value)
    }

    fun showDeleteItemConfDialog(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteItemConfDialog = value)
    }

    fun showDeleteItemPopup(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteItemPopup = value)
    }

    fun showFreightContent(value: Boolean) {
        _uiState.update { it.copy(showFreightContent = value) }
    }

    fun showZoomableImage(value: Boolean) {
        _uiState.value = _uiState.value.copy(showZoomableImageDialog = value)
    }

    fun updateCurrentFreight(freight: Freight) {
        launchCatching {
            _uiState.update { it.copy(currentFreight = freight) }
        }
    }

    fun updateCurrentFreightBeforeChange(freight: Freight) {
        _uiState.update { it.copy(currentFreightBeforeChange = freight) }
    }

    fun updateFreight(freight: Freight) {
        viewModelScope.launch { firestoreService.updateFreight(freight) }
    }

    fun updateSwipedItemId(id: String) {
        _uiState.update { it.copy(swipedItemId = id) }
    }
}


//    fun saveImageToCloud(uri: Uri) = launchCatching {
//        val downloadUri = storageService.saveImage(
//            fileUri = uri,
//            isAvatar = false
//        )
//        val uriList = _uiState.value.cloudUriList.toMutableList()
//        uriList.add(downloadUri)
//        _uiState.value = _uiState.value.copy(cloudUriList = uriList)
//    }