package com.example.toolsfordriver.ui.screens.tripsreport

import androidx.lifecycle.ViewModel
import com.example.toolsfordriver.common.calcDuration
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.data.model.service.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TripsReportViewModel @Inject constructor(
    private val firestoreService: FirestoreService
) : ViewModel() {

    val trips = firestoreService.trips
    val users = firestoreService.users
    private val _uiState = MutableStateFlow(TripsReportUiState())
    val uiState = _uiState.asStateFlow()

    fun showDateRangePicker(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDateRangePicker = value)
    }

    fun updateDailyPayData(trips: List<Pair<Trip, Duration?>>, user: User) {
        _uiState.value = _uiState.value.copy(
            dailyPaymentDuration = {
                var duration = Duration.ZERO
                trips.forEach { trip -> trip.second?.let { duration += it } }
                duration
            }.invoke()
        )
        _uiState.value = _uiState.value.copy(
            earnings = calcEarnings(
                _uiState.value.dailyPaymentDuration,
                _uiState.value.hourlyPaymentDuration,
                user
            )
        )
    }

    fun updateHourlyPayData(trips: List<Pair<Trip, Duration?>>, user: User) {
        _uiState.value = _uiState.value.copy(
            hourlyPaymentDuration = {
                var duration = Duration.ZERO
                trips.forEach { trip -> trip.second?.let { duration += it } }
                duration
            }.invoke()
        )
        _uiState.value = _uiState.value.copy(
            earnings = calcEarnings(
                _uiState.value.dailyPaymentDuration,
                _uiState.value.hourlyPaymentDuration,
                user
            )
        )
    }

    fun getMappedTrips(
        trips: List<Trip>,
        start: Date,
        end: Date,
        timeZone: ZoneId,
        user: User
    ): Map<String, List<Pair<Trip, Duration?>>> {

        val daily = mutableListOf<Pair<Trip, Duration?>>()
        val hourly = mutableListOf<Pair<Trip, Duration?>>()

        trips.filter {
            if (it.startTime != null && it.endTime != null) {
                it.startTime < end && it.endTime > start
            } else if (it.startTime != null) {
                it.startTime >= start && it.startTime < end
            } else false
        }.forEach {
            var trip = it

            if (trip.endTime == null) {
                trip = trip.copy(
                    endTime = Date.from(Instant.now().atZone(timeZone).toInstant())
                )
            }

            trip.startTime?.let { startTime ->
                if (startTime < start) {
                    trip = trip.copy(startTime = start)
                }
            }
            trip.endTime?.let { endTime ->
                if (endTime > end) {
                    trip = trip.copy(endTime = end)
                }
            }

            val duration = calcDuration(
                LocalDateTime.ofInstant(trip.startTime?.toInstant(), timeZone),
                LocalDateTime.ofInstant(trip.endTime?.toInstant(), timeZone),
                user.roundUpFromMinutes
            )

            if (!trip.hourlyPayment) daily.add(trip to duration) else hourly.add(trip to duration)
        }

        return mapOf("d" to daily, "h" to hourly)
    }

    private fun calcEarnings(daysDuration: Duration, hoursDuration: Duration, user: User): Double {
        return hoursDuration.toHours() * user.paymentPerHour +
                daysDuration.toHours() * user.paymentPerDay / 24
    }
}