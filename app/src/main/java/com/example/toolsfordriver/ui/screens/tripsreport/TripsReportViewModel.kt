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
import java.time.LocalDateTime
import java.time.ZoneId
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
}