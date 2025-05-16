package com.example.toolsfordriver.ui.screens.trip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.calcEarnings
import com.example.toolsfordriver.common.calcPeriod
import com.example.toolsfordriver.common.formatPeriod
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
import kotlinx.datetime.LocalDateTime
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

    fun updateSwipedItemId(id: String) {
        _uiState.value = _uiState.value.copy(swipedItemId = id)
    }

    fun addTripToDelete(trip: Trip) {
        _uiState.value = _uiState.value.copy(tripToDelete = trip)
    }

    fun showDeleteItemConfDialog(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteItemConfDialog = value)
    }

    fun updateTripDuration(start: LocalDateTime?, end: LocalDateTime?) {
        val period = calcPeriod(start, end)
        _uiState.value = _uiState.value.copy(
            tripDuration = if (period != null) formatPeriod(period) else ""
        )
    }

    fun updateTripEarnings(start: LocalDateTime?, end: LocalDateTime?, perHour: Double) {
        val earnings = calcEarnings(start, end, perHour)
        _uiState.value = _uiState.value.copy(
            tripEarnings = if (earnings != null) "$earnings PLN" else ""
        )
    }

    fun deleteTrip() {
        launchCatching {
            _uiState.value.tripToDelete?.let { firestoreService.deleteTrip(it.id) }
            _uiState.value = _uiState.value.copy(tripToDelete = null)
            showDeleteItemConfDialog(false)
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