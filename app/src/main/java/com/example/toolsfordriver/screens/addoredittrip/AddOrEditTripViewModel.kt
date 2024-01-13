package com.example.toolsfordriver.screens.addoredittrip

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrEditTripViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val trip: MutableState<TripDBModel?> = mutableStateOf(null)

    suspend fun getTrip(tripId: String): TripDBModel? {
            trip.value = repository.getTripById(tripId)
            return trip.value
    }
}