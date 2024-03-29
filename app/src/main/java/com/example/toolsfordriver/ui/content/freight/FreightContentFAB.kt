package com.example.toolsfordriver.ui.content.freight

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.AppButton
import com.example.toolsfordriver.utils.deleteImageFromInternalStorage

@Composable
fun FreightContentFAB(
    viewModel: TFDViewModel,
    context: Context,
    imagesToDelete: MutableState<MutableList<Uri>>,
    onFABClicked: () -> Unit
) {
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!
    val isNewFreight = viewModel.uiState.value.isNewFreight

    AppButton(
        buttonText = stringResource(
            id = if (isNewFreight) R.string.add_freight else  R.string.update_freight
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 15.dp),
        enabled = freight.unloads.isNotEmpty() &&
                freight.distance != null && freight.distance != 0
    ) {
        val firstLoadTime = freight.loads.keys.min()
        val lastLoadTime = freight.loads.keys.max()
        val firstUnloadTime = freight.unloads.keys.min()
        val lastUnloadTime = freight.unloads.keys.max()

        val imagesToDeleteList = imagesToDelete.value.toMutableList()

        val amountImagesToDelete = imagesToDeleteList.size
        imagesToDelete.value.forEach { uri ->
            if (deleteImageFromInternalStorage(uri, context)) {
                imagesToDeleteList.remove(uri)
            }
        }

        if (imagesToDeleteList.isEmpty()) {
            if (amountImagesToDelete > 0) {
                Toast.makeText(
                    context,
                    context.getString(
                        if (amountImagesToDelete > 1) R.string.images else R.string.image
                    ) + context.getString(R.string.deleted),
                    Toast.LENGTH_LONG
                ).show()
            }
            if (firstLoadTime < lastUnloadTime
                && firstLoadTime < firstUnloadTime
                && lastLoadTime < lastUnloadTime
            ) {
                val freightUpdated = freight.copy()

                if (isNewFreight) {
                    viewModel.addFreight(freightUpdated)
                } else viewModel.updateFreight(freightUpdated)

                onFABClicked()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.load_unload_period_is_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_delete_attached_images),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}