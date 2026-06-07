package com.example.toolsfordriver.ui.screens.trip

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.ui.screens.trip.content.TripContent
import com.example.toolsfordriver.ui.screens.trip.content.TripListContent

@Composable
fun TripsScreen(
    adaptiveInfo: WindowAdaptiveInfo,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToTripsReportScreen: (String) -> Unit
) {
    val viewModel: TripViewModel = hiltViewModel()
    val showTripContent = viewModel.uiState.collectAsStateWithLifecycle().value.showTripContent

    if (showTripContent) {
        TripContent { viewModel.showTripContent(false) }
    } else {
        TripListContent (
            onNavIconClicked = onNavigateToHomeScreen,
            onNavigateToTripsReportScreen = onNavigateToTripsReportScreen
        )
    }
}
