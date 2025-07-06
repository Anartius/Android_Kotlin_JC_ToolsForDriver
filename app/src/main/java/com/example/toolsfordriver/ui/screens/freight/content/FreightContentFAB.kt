package com.example.toolsfordriver.ui.screens.freight.content

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.buttons.AppButton
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel

@Composable
fun FreightContentFAB(onFABClicked: () -> Unit) {
    val viewModel: FreightViewModel = hiltViewModel()
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val freight = uiState.currentFreight!!
    val isNewFreight = uiState.isNewFreight
    val imagesToDelete = uiState.imageUriToDeleteList

    AppButton(
        buttonText = stringResource(
            id = if (isNewFreight) R.string.add_freight else R.string.update_freight
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 15.dp),
        isEnabled = freight.unloads.isNotEmpty() &&
                freight.distance != null && freight.distance != 0
    ) {
        val firstLoadTime = freight.loads.keys.min()
        val lastLoadTime = freight.loads.keys.max()
        val firstUnloadTime = freight.unloads.keys.min()
        val lastUnloadTime = freight.unloads.keys.max()

        if (firstLoadTime < lastUnloadTime
            && firstLoadTime < firstUnloadTime
            && lastLoadTime < lastUnloadTime
        ) {
            if (isNewFreight) {
                viewModel.addFreight(freight)
            } else {
                if (imagesToDelete.isNotEmpty()) {
                    viewModel.deleteImagesAndUpdateFreight()
                } else viewModel.updateFreight(freight)

                viewModel.clearAddedUriList()
            }

            onFABClicked()

        } else {
            Toast.makeText(
                context,
                context.getString(R.string.load_unload_period_is_wrong),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}