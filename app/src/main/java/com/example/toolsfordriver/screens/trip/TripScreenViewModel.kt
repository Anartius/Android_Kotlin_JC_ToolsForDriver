package com.example.toolsfordriver.screens.trip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripScreenViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val id = checkNotNull(savedStateHandle.get<String>("tripId"))
    private val _tripList = MutableStateFlow<List<TripDBModel>>(emptyList())
    val tripList = _tripList.asStateFlow()

    init {
        viewModelScope.launch {
            _tripList.value = listOf(repository.getTripById(id))
        }
    }

    fun addTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.addTrip(trip) }
    }

    fun updateTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.updateTrip(trip) }
    }
}