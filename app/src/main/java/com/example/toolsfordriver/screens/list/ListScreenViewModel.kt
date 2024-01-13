package com.example.toolsfordriver.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _tripList = MutableStateFlow<List<TripDBModel>>(emptyList())
    val tripList = _tripList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTrips().distinctUntilChanged()
                .collect { listOfTrips ->
                    if (listOfTrips.isEmpty()) {
                        Log.d("List Of Trips", ": Empty list")
                        _tripList.value = emptyList()
                    } else _tripList.value = listOfTrips
                }
        }
    }

    fun addTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.addTrip(trip) }
    }

    fun deleteTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.deleteTrip(trip) }
    }
}