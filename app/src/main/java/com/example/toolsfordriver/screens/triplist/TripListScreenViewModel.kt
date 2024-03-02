package com.example.toolsfordriver.screens.triplist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListScreenViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _tripList = MutableStateFlow<List<TripDBModel>>(emptyList())
    val tripList = _tripList.asStateFlow()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        viewModelScope.launch {
            repository.getAllTripsByUserId(userId).distinctUntilChanged().collect { listOfTrips ->
                if (listOfTrips.isEmpty()) {
                    Log.d("List Of Trips", ": Empty list")
                    _tripList.value = emptyList()
                } else _tripList.value = listOfTrips
            }
        }
    }

    fun deleteTrip(trip: TripDBModel) {
        viewModelScope.launch { repository.deleteTrip(trip) }
    }
}