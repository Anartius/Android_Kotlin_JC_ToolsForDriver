package com.example.toolsfordriver.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.content.TripListContent
import com.example.toolsfordriver.ui.content.trip.TripContent

@Composable
fun TripsScreen(onNavIconClicked: () -> Unit) {
    val viewModel: TFDViewModel = hiltViewModel()
    val showTripContent = viewModel.uiState.collectAsStateWithLifecycle().value.showTripContent

    if (showTripContent) {
        TripContent(viewModel = viewModel) { viewModel.showTripContent(false) }
    } else {
        TripListContent(viewModel = viewModel) { onNavIconClicked.invoke() }
    }
}
