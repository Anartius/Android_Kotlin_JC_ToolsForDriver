package com.example.toolsfordriver.ui.screens.freight.content

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.DeleteItemPopup
import com.example.toolsfordriver.ui.common.dialogs.ImageSourcePickerDialog
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel
import kotlinx.datetime.LocalDateTime

@Composable
fun FreightContentLazyColumn(viewModel: FreightViewModel) {

    BackHandler { viewModel.showFreightContent(false) }

    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight

    if (freight != null) {
        val context = LocalContext.current
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

        val selectedItemLocation = remember { mutableStateOf("") }
        val selectedItemDateTime = remember { mutableStateOf<LocalDateTime?>(null) }

        val showTimeLocationDialog = rememberSaveable { mutableStateOf(false) }
        val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
        val showNoteDialog = rememberSaveable { mutableStateOf(false) }
        val showSourcePickerDialog = rememberSaveable { mutableStateOf(false) }
        val showDeleteItemPopup = uiState.showDeleteItemPopup
        val showDeleteImagePopup = uiState.showDeleteImagePopup

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            uri?.let { viewModel.addImageToCurrentFreight(uri = it, context = context) }
            showSourcePickerDialog.value = false
        }

        val isLoadDialog = rememberSaveable { mutableStateOf(true) }
        val isLoadContent = rememberSaveable { mutableStateOf(true) }
        val deleteItemKey = rememberSaveable { mutableStateOf<Long?>(null) }
        val imageUriToDelete = rememberSaveable { mutableStateOf<Uri?>(null) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            item {
                ItemTitleRow(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    title = stringResource(id = R.string.loads),
                    icon = Icons.Filled.Add,
                    showIcon = true,
                    iconDescription = stringResource(id = R.string.add_place_and_time)
                ) {
                    selectedItemDateTime.value = null
                    selectedItemLocation.value = ""
                    isLoadDialog.value = true
                    showTimeLocationDialog.value = true
                }
            }

            items(items = freight.loads.toList().sortedBy { it.first }) { item ->
                isLoadContent.value = true

                LoadUnloadRow(
                    isLoad = true,
                    isLoadContent = isLoadContent,
                    time = item.first.toLong(),
                    isLoadDialog = isLoadDialog,
                    showTimeLocationDialog = showTimeLocationDialog,
                    selectedItemLocation = selectedItemLocation,
                    selectedItemDateTime = selectedItemDateTime,
                    deleteItemKey = deleteItemKey,
                    viewModel = viewModel
                ) { viewModel.showDeleteItemPopup(true) }
            }

            item {
                ItemTitleRow(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    title = stringResource(id = R.string.unloads),
                    icon = Icons.Filled.Add,
                    showIcon = true,
                    iconDescription = stringResource(id = R.string.add_place_and_time)
                ) {
                    selectedItemDateTime.value = null
                    selectedItemLocation.value = ""
                    isLoadDialog.value = false
                    showTimeLocationDialog.value = true
                }
            }

            items(items = freight.unloads.toList().sortedBy { it.first }) { item ->
                isLoadContent.value = false

                LoadUnloadRow(
                    isLoad = false,
                    isLoadContent = isLoadContent,
                    time = item.first.toLong(),
                    isLoadDialog = isLoadDialog,
                    showTimeLocationDialog = showTimeLocationDialog,
                    selectedItemLocation = selectedItemLocation,
                    selectedItemDateTime = selectedItemDateTime,
                    deleteItemKey = deleteItemKey,
                    viewModel = viewModel
                ) { viewModel.showDeleteItemPopup(true) }
            }

            item { DistanceContent(viewModel = viewModel) }

            item {
                ItemTitleRow(
                    modifier = Modifier.padding(start = 24.dp, bottom = 4.dp, end = 24.dp),
                    title = stringResource(id = R.string.pictures),
                    icon = Icons.Filled.Add,
                    showIcon = true,
                    iconDescription = stringResource(id = R.string.add_picture)
                ) {
                    showSourcePickerDialog.value = true
                }
            }

            items(items = freight.imagesUriList) { uri ->
                ImageItem(
                    uri = uri,
                    context = context,
                    onClick = {
                        viewModel.setZoomableImageUri(uri.toUri())
                        viewModel.showFreightContent(false)
                        viewModel.showZoomableImage(true)
                    },
                    onLongClick = {
                        imageUriToDelete.value = uri.toUri()
                        viewModel.showDeleteImagePopup(true)
                    }
                )
            }

            item {
                NoteContent(
                    viewModel = viewModel,
                    showNoteDialog = showNoteDialog
                )
            }
        }

        TimeLocationDialog(
            isLoadDialog = isLoadDialog,
            showDialog = showTimeLocationDialog,
            showDateTimeDialog = showDateTimeDialog,
            location = selectedItemLocation,
            dateTime = selectedItemDateTime,
            viewModel = viewModel,
            context = context
        )

        TextInputDialog(
            viewModel = viewModel,
            showDialog = showNoteDialog
        )

        if (showDeleteItemPopup) {
            DeleteItemPopup(
                itemName = stringResource(id = if (isLoadContent.value) R.string.load else R.string.unload),
                onDismiss = { viewModel.showDeleteItemPopup(false) }
            ) {
                deleteItemKey.value?.let {
                    val itemMap = (if (isLoadContent.value) {
                        freight.loads
                    } else freight.unloads).toMutableMap()

                    itemMap.remove(deleteItemKey.value.toString())

                    viewModel.updateCurrentFreight(
                        freight.copy(
                            loads = if (isLoadContent.value) itemMap.toMap() else freight.loads,
                            unloads = if (!isLoadContent.value) itemMap.toMap() else freight.unloads
                        )
                    )
                }
                deleteItemKey.value = null
                viewModel.showDeleteItemPopup(false)
            }
        }

        if (showDeleteImagePopup) {
            DeleteItemPopup(
                itemName = stringResource(id = R.string.image),
                onDismiss = { viewModel.showDeleteImagePopup(false) }
            ) {
                val imagesToDelete = uiState.imageUriToDeleteList.toMutableList()

                imageUriToDelete.value?.let { uri ->
                    val imageUriList = freight.imagesUriList.toMutableList()
                    imageUriList.remove(imageUriToDelete.value.toString())

                    viewModel.updateCurrentFreight(freight.copy(imagesUriList = imageUriList))

                    imagesToDelete.add(uri)
                    viewModel.addImageUriToDelete(imagesToDelete)
                }
                imageUriToDelete.value = null
                viewModel.showDeleteImagePopup(false)
            }
        }

        if (showSourcePickerDialog.value) {
            ImageSourcePickerDialog(
                showDialog = showSourcePickerDialog,
                showCamera = { viewModel.showCamera(true) },
                showGallery = { launcher.launch("image/*") }
            )
        }
    }
}