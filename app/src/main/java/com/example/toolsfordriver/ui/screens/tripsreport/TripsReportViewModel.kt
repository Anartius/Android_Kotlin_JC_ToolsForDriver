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
import kotlin.collections.forEach

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

    fun updateDailyPayData(trips: List<Trip>, user: User, timeZone: ZoneId) {
        _uiState.value = _uiState.value.copy(
            dailyPaymentDuration = getDuration(trips, timeZone, user.roundUpFromMinutes)
        )
        _uiState.value = _uiState.value.copy(
            earnings = calcEarnings(
                _uiState.value.dailyPaymentDuration,
                _uiState.value.hourlyPaymentDuration,
                user
            )
        )
    }

    fun updateHourlyPayData(trips: List<Trip>, user: User, timeZone: ZoneId) {
        _uiState.value = _uiState.value.copy(
            hourlyPaymentDuration = getDuration(trips, timeZone, user.roundUpFromMinutes)
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
        timeZone: ZoneId
    ): Map<String, List<Trip>> {
        val daily = mutableListOf<Trip>()
        val hourly = mutableListOf<Trip>()

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

            if (!trip.hourlyPayment) daily.add(trip) else hourly.add(trip)
        }

        return mapOf("d" to daily, "h" to hourly)
    }

    private fun getDuration(trips: List<Trip>, timeZone: ZoneId, roundUpFromMin: Int): Duration {
        var duration = Duration.ZERO

        trips.forEach {
            if (it.startTime != null && it.endTime != null) {
                val start = LocalDateTime.ofInstant(it.startTime.toInstant(), timeZone)
                val end = LocalDateTime.ofInstant(it.endTime.toInstant(), timeZone)

                duration += calcDuration(start, end, roundUpFromMin)
            }
        }
        return duration
    }

    private fun calcEarnings(daysDuration: Duration, hoursDuration: Duration, user: User): Double {
        return hoursDuration.toHours() * user.paymentPerHour +
                daysDuration.toHours() * user.paymentPerDay / 24
    }
}