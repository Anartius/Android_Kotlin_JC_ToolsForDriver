package com.example.toolsfordriver.screens.freight

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.components.AppButton
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.utils.deleteImageFromInternalStorage

@Composable
fun FreightScreenFABContent(
    isCreateFreight: MutableState<Boolean>,
    freight: MutableState<FreightDBModel>,
    navController: NavController,
    viewModel: FreightScreenViewModel,
    context: Context,
    imagesToDelete: MutableState<MutableList<Uri>>
) {
    AppButton(
        buttonText = if (isCreateFreight.value) "Add Freight" else "Update Freight",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 15.dp),
        enabled = freight.value.loads.isNotEmpty() &&
                freight.value.unloads.isNotEmpty() &&
                freight.value.distance != null && freight.value.distance != 0
    ) {
        val firstLoadTime = freight.value.loads.keys.min()
        val lastLoadTime = freight.value.loads.keys.max()
        val firstUnloadTime = freight.value.unloads.keys.min()
        val lastUnloadTime = freight.value.unloads.keys.max()

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
                    "Image${if (amountImagesToDelete > 1) "s" else ""} deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (firstLoadTime < lastUnloadTime
                && firstLoadTime < firstUnloadTime
                && lastLoadTime < lastUnloadTime
            ) {
                val freightUpdated = freight.value.copy()

                if (isCreateFreight.value) {
                    viewModel.addFreight(freightUpdated)
                } else viewModel.updateFreight(freightUpdated)
                navController.popBackStack()
            } else {
                Toast.makeText(
                    context,
                    "Load - unload period is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Failed to delete attached images",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}