package com.example.toolsfordriver.ui.screens.trip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.calcDuration
import com.example.toolsfordriver.common.calcEarnings
import com.example.toolsfordriver.common.durationAsString
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.data.model.User
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
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
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

    fun addTrip(trip: Trip) {
        launchCatching { firestoreService.saveTrip(trip) }
    }

    fun addTripToDelete(trip: Trip) {
        _uiState.value = _uiState.value.copy(tripToDelete = trip)
    }

    fun deleteTrip(successMsg: String = "") {
        launchCatching {
            _uiState.value.tripToDelete?.let { firestoreService.deleteTrip(it.id) }
            _uiState.value = _uiState.value.copy(tripToDelete = null)
            showDeleteItemConfDialog(false)
            snackbarChannel.send(UiText.DynamicString(successMsg))
        }
    }

    fun setCurrentTripAsNew(value: Boolean) {
        _uiState.update { it.copy(isNewTrip = value) }
    }

    fun showDeleteItemConfDialog(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteItemConfDialog = value)
    }

    fun showTripContent(value: Boolean) {
        _uiState.update { it.copy(showTripContent = value) }
    }

    fun updateCurrentTripBeforeChange(trip: Trip?) {
        _uiState.update { it.copy(currentTripBeforeChange = trip) }
    }

    fun updateCurrentTrip(trip: Trip?) {
        _uiState.update { it.copy(currentTrip = trip) }
    }

    fun updateSwipedItemId(id: String) {
        _uiState.value = _uiState.value.copy(swipedItemId = id)
    }

    fun updateTrip(trip: Trip) {
        launchCatching { firestoreService.updateTrip(trip) }
    }

    fun updateTripDuration(
        start: LocalDateTime?,
        end: LocalDateTime?,
        roundUpFromMinutes: Int
    ) {
        val duration = calcDuration(start, end, roundUpFromMinutes)
        _uiState.value = _uiState.value.copy(
            tripDuration = if (duration != null) durationAsString(duration) else ""
        )
    }

    fun updateTripEarnings(
        start: LocalDateTime?,
        end: LocalDateTime?,
        roundUpFromMinutes: Int?,
        moneyPerHour: Double
    ) {
        val earnings = calcEarnings(start, end, roundUpFromMinutes ?: 0, moneyPerHour)
        _uiState.value = _uiState.value.copy(
            tripEarnings = if (earnings != null) "$earnings PLN" else ""
        )
    }

    fun calcDayPaymentDuration(
        trips: List<Trip>,
        timeZone: ZoneId,
        roundUpFromMinutes: Int
    ): Duration {
        var durationPerDay = Duration.ZERO

        trips.forEach {
            if (it.startTime != null && it.endTime != null) {
                val start = LocalDateTime.ofInstant(it.startTime.toInstant(), timeZone)
                val end = LocalDateTime.ofInstant(it.endTime.toInstant(), timeZone)
                val duration = calcDuration(start, end, roundUpFromMinutes)

                if (!it.hourlyPayment) {
                    durationPerDay += duration
                }
            }
        }

        return durationPerDay
    }

    fun calcHourPaymentDuration(
        trips: List<Trip>,
        timeZone: ZoneId,
        roundUpFromMinutes: Int
    ): Duration {
        var durationPerHour = Duration.ZERO

        trips.forEach {
            if (it.startTime != null && it.endTime != null) {
                val start = LocalDateTime.ofInstant(it.startTime.toInstant(), timeZone)
                val end = LocalDateTime.ofInstant(it.endTime.toInstant(), timeZone)
                val duration = calcDuration(start, end, roundUpFromMinutes)

                if (it.hourlyPayment) {
                    durationPerHour += duration
                }
            }
        }

        return durationPerHour
    }

    fun calcEarnings(daysDuration: Duration, hoursDuration: Duration, user: User): Double {
        return hoursDuration.toHours() * user.paymentPerHour +
                daysDuration.toHours() * user.paymentPerDay / 24
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