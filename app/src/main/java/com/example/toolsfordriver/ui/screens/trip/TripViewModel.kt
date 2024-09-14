package com.example.toolsfordriver.ui.screens.trip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.data.model.service.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val firestoreService: FirestoreService
) : ViewModel() {

    private val snackbarChannel = Channel<UiText>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()
    private val _uiState = MutableStateFlow(TripUiState())
    val uiState = _uiState.asStateFlow()
    val trips = firestoreService.trips
    val users = firestoreService.users

    fun updateCurrentTrip(trip: Trip) {
        _uiState.update { it.copy(currentTrip = trip) }
    }

    fun showTripContent(value: Boolean) {
        _uiState.update { it.copy(showTripContent = value) }
    }

    fun setCurrentTripAsNew(value: Boolean) {
        _uiState.update { it.copy(isNewTrip = value) }
    }

    fun addTrip(trip: Trip) {
        launchCatching { firestoreService.saveTrip(trip) }
    }

    fun updateTrip(trip: Trip) {
        launchCatching { firestoreService.updateTrip(trip) }
    }

    fun addTripToDelete(trip: Trip) {
        _uiState.value = _uiState.value.copy(tripToDelete = trip)
    }

    fun showDeletePopup(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDeletePopup = value)
    }

    fun deleteTrip() {
        launchCatching {
            _uiState.value.tripToDelete?.let { firestoreService.deleteTrip(it.id) }
            _uiState.value = _uiState.value.copy(tripToDelete = null)
            showDeletePopup(false)
        }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler{ _, throwable ->
                viewModelScope.launch {
                    Log.e("Firestore", throwable.message.toString())
                    snackbarChannel.send(UiText.DynamicString(throwable.message.toString()))
                }
            },
            block = block
        )
    }
}


//    init {
//        initializeUiState()
//    }
//
//    private fun initializeUiState() {
//        viewModelScope.launch {
//            tripRepository
//                .getAllTripsByUserId(userId).distinctUntilChanged().collect { listOfTrips ->
//                    if (listOfTrips.isEmpty()) {
//                        Log.d("List Of Trips", ": Empty list")
//                        _tripList.value = emptyList()
//                    } else {
//                        _tripList.value = listOfTrips
//                        _uiState.value = _uiState.value.copy(
//                            trips = mutableStateOf(_tripList.value)
//                        )
//                    }
//                }
//        }
//    }
