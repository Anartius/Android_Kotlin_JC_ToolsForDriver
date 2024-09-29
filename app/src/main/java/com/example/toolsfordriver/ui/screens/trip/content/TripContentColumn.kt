package com.example.toolsfordriver.ui.screens.trip.content

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.calcEarnings
import com.example.toolsfordriver.common.calcPeriod
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.dateAsStringIso
import com.example.toolsfordriver.common.formatPeriod
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.ui.common.AppButton
import com.example.toolsfordriver.ui.common.TextRow
import com.example.toolsfordriver.ui.common.dialogs.DateTimeDialog
import com.example.toolsfordriver.ui.screens.trip.TripViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun TripContentColumn(viewModel: TripViewModel) {
    val showDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    val isStartDatePickerDialog = remember { mutableStateOf(true) }

    val trip = viewModel.uiState.collectAsStateWithLifecycle().value.currentTrip
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value

    if (trip != null && users.isNotEmpty()) {
        val user = users.first()

        val paymentPerHour =
            try {
                if (trip.hourlyPayment) user.paymentPerHour else user.paymentPerDay / 24
            } catch (e: Exception) {
                Log.e("User Payment", e.message.toString())
                0.0
            }

        val isNewTrip = viewModel.uiState.collectAsStateWithLifecycle().value.isNewTrip

        val startDateTime = remember(trip) {
            mutableStateOf(
                if (trip.startTime != null) {
                    LocalDateTime(
                        LocalDate.parse(dateAsStringIso(trip.startTime)),
                        LocalTime.parse(timeAsString(trip.startTime))
                    )
                } else null
            )
        }

        val endDateTime = remember(trip) {
            mutableStateOf(
                if (trip.endTime != null) {
                    LocalDateTime(
                        LocalDate.parse(dateAsStringIso(trip.endTime)),
                        LocalTime.parse(timeAsString(trip.endTime))
                    )
                } else null
            )
        }

        val tripDuration = remember(trip) {
            mutableStateOf(calcPeriod(startDateTime.value, endDateTime.value))
        }

        val earnings = remember(trip) {
            mutableStateOf(
                calcEarnings(startDateTime.value, endDateTime.value, (paymentPerHour))
            )
        }

        TextRow(
            valueDescription = stringResource(R.string.start),
            value = "${
                dateAsString(startDateTime.value?.toInstant(
                    TimeZone.currentSystemDefault())?.toEpochMilliseconds())
            }  "
                    + "${startDateTime.value?.time ?: ""}",
            clickable = true,
            showIcon = true
        ) {
            isStartDatePickerDialog.value = true
            showDatePickerDialog.value = true
        }
        TextRow(
            valueDescription = stringResource(R.string.finish),
            value = "${
                dateAsString(endDateTime.value?.toInstant(
                    TimeZone.currentSystemDefault())?.toEpochMilliseconds())
            }  "
                    + "${endDateTime.value?.time ?: ""}",
            clickable = true,
            showIcon = true
        ) {
            isStartDatePickerDialog.value = false
            showDatePickerDialog.value = true
        }

        TextRowWithDropdownMenu(
            text = stringResource(R.string.payment),
            trip = trip
        )

        TextRow(
            valueDescription = stringResource(R.string.duration),
            value = if (tripDuration.value != null) {
                formatPeriod(tripDuration.value!!)
            } else ""
        )

        TextRow(
            valueDescription = stringResource(R.string.earnings),
            value = if (earnings.value != null) {
                "${earnings.value} PLN"
            } else ""
        )

        Spacer(modifier = Modifier.height(40.dp))

        AppButton(
            buttonText = stringResource(
                id = if (isNewTrip) R.string.add_trip else R.string.update_trip
            ),
            enabled = trip.startTime != null && trip.endTime == null ||
                    trip.startTime != null && trip.endTime != null &&
                    (trip.startTime < trip.endTime)
        ) {
            if (isNewTrip) viewModel.addTrip(trip) else viewModel.updateTrip(trip)

            viewModel.showTripContent(false)
        }

        DateTimeDialog(
            showDialog = showDatePickerDialog,
            dateTime = if (isStartDatePickerDialog.value) startDateTime else endDateTime
        ) {
            viewModel.updateCurrentTrip(
                if (isStartDatePickerDialog.value) {
                    trip.copy(
                        startTime = startDateTime.value?.toInstant(
                            TimeZone.currentSystemDefault())?.toEpochMilliseconds()
                    )
                } else {
                    trip.copy(
                        endTime = endDateTime.value?.toInstant(
                            TimeZone.currentSystemDefault())?.toEpochMilliseconds()
                    )
                }
            )
        }
    }
}