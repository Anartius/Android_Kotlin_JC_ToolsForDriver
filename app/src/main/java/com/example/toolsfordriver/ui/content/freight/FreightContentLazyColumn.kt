package com.example.toolsfordriver.ui.content.freight

import android.net.Uri
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.DeleteItemPopup
import kotlinx.datetime.LocalDateTime


@Composable
fun FreightContentLazyColumn(
    viewModel: TFDViewModel,
    imagesToDelete: MutableState<MutableList<Uri>>
) {
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight
    if (freight != null) {
        val context = LocalContext.current

        val selectedItemLocation = remember { mutableStateOf("") }
        val selectedItemDateTime = remember { mutableStateOf<LocalDateTime?>(null) }
        val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
        val dialogImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            uri?.let {
                imageUri.value = viewModel.saveImageToInternalStorage(context = context, uri = uri)

                val freightUriList = freight.pictureUri.toMutableList()
                freightUriList.add(imageUri.value.toString())

                viewModel.updateCurrentFreight(
                    freight.copy(pictureUri = freightUriList.toList())
                )
            }
        }

        val showTimeLocationDialog = rememberSaveable { mutableStateOf(false) }
        val showDateTimeDialog = rememberSaveable { mutableStateOf(false) }
        val showNoteDialog = rememberSaveable { mutableStateOf(false) }
        val showDeletePopup = rememberSaveable { mutableStateOf(false) }
        val showDeleteImagePopup = rememberSaveable { mutableStateOf(false) }
        val showZoomableImageDialog = rememberSaveable { mutableStateOf(false) }

        val isLoadDialog = rememberSaveable { mutableStateOf(true) }
        val isLoadContent = rememberSaveable { mutableStateOf(true) }
        val deleteItemKey = rememberSaveable { mutableStateOf<Long?>(null) }
        val imageToDeleteFromUriList = rememberSaveable { mutableStateOf<Uri?>(null) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            item {
                ItemsTitleRow(
                    modifier = Modifier.padding(bottom = 12.dp),
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
                    time = item.first,
                    isLoadDialog = isLoadDialog,
                    showTimeLocationDialog = showTimeLocationDialog,
                    selectedItemLocation = selectedItemLocation,
                    selectedItemDateTime = selectedItemDateTime,
                    showDeletePopup = showDeletePopup,
                    deleteItemKey = deleteItemKey,
                    viewModel = viewModel
                )
            }

            item {
                ItemsTitleRow(
                    modifier = Modifier.padding(bottom = 12.dp),
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
                    time = item.first,
                    isLoadDialog = isLoadDialog,
                    showTimeLocationDialog = showTimeLocationDialog,
                    selectedItemLocation = selectedItemLocation,
                    selectedItemDateTime = selectedItemDateTime,
                    showDeletePopup = showDeletePopup,
                    deleteItemKey = deleteItemKey,
                    viewModel = viewModel
                )
            }

            item { DistanceContent(viewModel = viewModel) }

            item {
                ItemsTitleRow(
                    modifier = Modifier.padding(bottom = 12.dp),
                    title = stringResource(id = R.string.pictures),
                    icon = Icons.Filled.Add,
                    showIcon = true,
                    iconDescription = stringResource(id = R.string.add_picture)
                ) {
                    launcher.launch("image/*")
                }
            }

            items(items = freight.pictureUri) { item ->
                PictureItem(
                    uri = item,
                    dialogImageUri = dialogImageUri,
                    showZoomableImageDialog = showZoomableImageDialog,
                    showDeleteImagePopup = showDeleteImagePopup,
                    imageUriToDelete = imageToDeleteFromUriList,
                    context = context
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

        ZoomableImageDialog(
            showDialog = showZoomableImageDialog,
            imageUri = dialogImageUri.value
        )

        TextInputDialog(
            viewModel = viewModel,
            showDialog = showNoteDialog
        )

        DeleteItemPopup(
            showDeletePopup = showDeletePopup,
            itemName = stringResource(id = if (isLoadContent.value) R.string.load else R.string.unload)
        ) {
            deleteItemKey.value?.let {
                val itemMap = (if (isLoadContent.value) {
                    freight.loads
                } else freight.unloads).toMutableMap()

                itemMap.remove(deleteItemKey.value)

                viewModel.updateCurrentFreight(
                    freight.copy(
                        loads = if (isLoadContent.value) itemMap.toMap() else freight.loads,
                        unloads = if (!isLoadContent.value) itemMap.toMap() else freight.unloads
                    )
                )
            }
            deleteItemKey.value = null
            showDeletePopup.value = false
        }

        DeleteItemPopup(
            showDeletePopup = showDeleteImagePopup,
            itemName = stringResource(id = R.string.image)
        ) {
            imageToDeleteFromUriList.value?.let { uri ->
                val imageUriList = freight.pictureUri.toMutableList()
                imageUriList.remove(imageToDeleteFromUriList.value.toString())

                viewModel.updateCurrentFreight(freight.copy(pictureUri = imageUriList))

                imagesToDelete.value.add(uri)
            }
            imageToDeleteFromUriList.value = null
            showDeleteImagePopup.value = false
        }
    }
}