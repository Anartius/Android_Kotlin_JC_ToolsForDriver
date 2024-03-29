package com.example.toolsfordriver.ui.content.trip

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
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.AppButton
import com.example.toolsfordriver.ui.components.DateTimeDialog
import com.example.toolsfordriver.ui.components.TextRow
import com.example.toolsfordriver.utils.calcEarnings
import com.example.toolsfordriver.utils.calcPeriod
import com.example.toolsfordriver.utils.dateAsString
import com.example.toolsfordriver.utils.dateAsStringIso
import com.example.toolsfordriver.utils.formatPeriod
import com.example.toolsfordriver.utils.timeAsString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


@Composable
fun TripContentColumn(viewModel: TFDViewModel) {
    val showDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    val isStartDatePickerDialog = remember { mutableStateOf(true) }

    val trip = viewModel.uiState.collectAsStateWithLifecycle().value.currentTrip

    if (trip != null) {
        val isNewTrip = viewModel.uiState.value.isNewTrip

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
                calcEarnings(startDateTime.value, endDateTime.value, (400.0 / 24))
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
