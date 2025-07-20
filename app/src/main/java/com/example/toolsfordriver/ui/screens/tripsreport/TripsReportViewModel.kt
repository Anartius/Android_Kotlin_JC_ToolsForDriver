package com.example.toolsfordriver.ui.screens.tripsreport

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.UiText
import com.example.toolsfordriver.common.calcDuration
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.durationAsString
import com.example.toolsfordriver.common.timeAsString
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
import kotlinx.coroutines.launch
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

    private val snackbarChannel = Channel<UiText>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()

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

    fun copyDataToClipboard(
        tripsMap: Map<String, List<Pair<Trip, Duration?>>>,
        context: Context
    ): String {
        var result = ""
        var duration = Duration.ZERO

        launchCatching {
            val dailyTrips = tripsMap["d"]
            val hourlyTrips = tripsMap["h"]

            dailyTrips?.let { trips ->
                result += "${context.getString(R.string.payment_per_day)}:\n"

                trips.forEach { trip ->
                    duration += trip.second ?: Duration.ZERO

                    result += "${dateAsString(trip.first.startTime)} " +
                            timeAsString(trip.first.startTime) +
                            " - ${dateAsString(trip.first.endTime)} " +
                            timeAsString(trip.first.endTime) +
                            "  ${durationAsString(trip.second, context)}\n"
                }
            }

            hourlyTrips?.let { trips ->
                result += "${context.getString(R.string.payment_per_hour)}:\n"

                trips.forEach { trip ->
                    duration += trip.second ?: Duration.ZERO

                    result += "${dateAsString(trip.first.startTime)} " +
                            timeAsString(trip.first.startTime) +
                            " - ${dateAsString(trip.first.endTime)} " +
                            timeAsString(trip.first.endTime) +
                            "  ${durationAsString(trip.second, context)}\n"
                }
            }

            if (result.isNotEmpty()) {
                result += "${context.getString(R.string.summary)}: " +
                        durationAsString(duration, context)

                snackbarChannel.send(
                    UiText.StringResource(R.string.data_copied_to_clipboard)
                )
            }
        }

        return result
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler{ _, throwable ->
                viewModelScope.launch {
                    Log.e("Error", throwable.message.toString())
                    snackbarChannel.send(
                        UiText.DynamicString(throwable.message.toString())
                    )
                }
            },
            block = block
        )
    }
}