package com.example.toolsfordriver.ui.screens.trip

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.toolsfordriver.data.model.Trip

data class TripUiState(
    val trips: MutableState<List<Trip>> = mutableStateOf(emptyList()),
    val currentTrip: Trip? = null,
    val isNewTrip: Boolean = false,
    val showTripContent: Boolean = false,
    val tripToDelete: Trip? = null,
    val showDeletePopup: Boolean = false,
    val tripDuration: String = ""
)
