package com.example.toolsfordriver.ui.screens.trip

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.ui.screens.trip.content.TripContent
import com.example.toolsfordriver.ui.screens.trip.content.TripListContent

@Composable
fun TripsScreen(onNavIconClicked: () -> Unit) {
    val viewModel: TripViewModel = hiltViewModel()
    val showTripContent = viewModel.uiState.collectAsStateWithLifecycle().value.showTripContent

    if (showTripContent) {
        TripContent { viewModel.showTripContent(false) }
    } else {
        TripListContent { onNavIconClicked.invoke() }
    }
}
