package com.example.toolsfordriver.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.data.TripDBModel

data class TFDUiState(
    val trips: MutableState<List<TripDBModel>> = mutableStateOf(emptyList()),
    val currentTrip: TripDBModel? = null,
    val isNewTrip: Boolean = false,
    val showTripContent: Boolean = false,
    val freights: MutableState<List<FreightDBModel>> = mutableStateOf(emptyList()),
    val currentFreight: FreightDBModel? = null,
    val isNewFreight: Boolean = false,
    val showFreightContent: Boolean = false
)