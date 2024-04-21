package com.example.toolsfordriver.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.content.FreightListContent
import com.example.toolsfordriver.ui.content.freight.FreightContent

@Composable
fun FreightsScreen(onNavIconClicked: () -> Unit) {
    val viewModel: TFDViewModel = hiltViewModel()
    val showFreightContent =
        viewModel.uiState.collectAsStateWithLifecycle().value.showFreightContent

    if (showFreightContent) {
        FreightContent(viewModel = viewModel) { viewModel.showFreightContent(false) }
    } else {
        FreightListContent(viewModel = viewModel) { onNavIconClicked.invoke() }
    }
}