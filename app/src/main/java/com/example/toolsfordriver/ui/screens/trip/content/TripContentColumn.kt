package com.example.toolsfordriver.ui.screens.trip.content

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.ui.common.AppButton
import com.example.toolsfordriver.ui.common.TextRow
import com.example.toolsfordriver.ui.common.dialogs.DateTimeDialog
import com.example.toolsfordriver.ui.screens.trip.TripViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

@Composable
fun TripContentColumn() {
    val viewModel: TripViewModel = hiltViewModel()
    val timeZoneId = ZoneId.systemDefault()
    val showDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    var isStartDatePickerDialog by remember { mutableStateOf(true) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val trip = uiState.currentTrip
    val tripDuration = uiState.tripDuration
    val tripEarnings = uiState.tripEarnings

    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value

    if (trip != null && users.isNotEmpty()) {
        val user = users.first()
        val roundUpFromMinutes = user.roundUpFromMinutes
        val isNewTrip = uiState.isNewTrip

        val paymentPerHour =
            try {
                if (trip.hourlyPayment) user.paymentPerHour else user.paymentPerDay / 24
            } catch (e: Exception) {
                Log.e("User Payment", e.message.toString())
                0.0
            }

        val startDateTime = remember(trip) {
            mutableStateOf(
                if (trip.startTime != null) {
                    LocalDateTime.ofInstant(trip.startTime.toInstant(), timeZoneId)
                } else null
            )
        }

        val endDateTime = remember(trip) {
            mutableStateOf(
                if (trip.endTime != null) {
                    LocalDateTime.ofInstant(trip.endTime.toInstant(), timeZoneId)
                } else null
            )
        }

        LaunchedEffect(startDateTime, endDateTime, paymentPerHour) {
            viewModel.updateTripDuration(
                startDateTime.value, endDateTime.value, roundUpFromMinutes
            )
            viewModel.updateTripEarnings(
                startDateTime.value, endDateTime.value, roundUpFromMinutes, paymentPerHour)
        }

        var isStartDateTimeExist by remember(startDateTime) {
            mutableStateOf(startDateTime.value != null)
        }

        var isEndDateTimeExist by remember(endDateTime) {
            mutableStateOf(endDateTime.value != null)
        }

        var isEndBeforeStartOrEqual by remember(startDateTime, endDateTime) {
            mutableStateOf(
                isStartDateTimeExist && isEndDateTimeExist && trip.startTime!! >= trip.endTime!!
            )
        }

        TextRow(
            valueDescription = stringResource(R.string.start),
            value = dateAsString(startDateTime.value) + " " + timeAsString(startDateTime.value),
            clickable = true,
            showIcon = true,
            firstTextColor = colorResource(R.color.light_blue)
        ) {
            isStartDatePickerDialog = true
            showDatePickerDialog.value = true
        }

        TextRow(
            valueDescription = stringResource(R.string.end),
            value = dateAsString(endDateTime.value) + " " + timeAsString(endDateTime.value),
            clickable = isStartDateTimeExist,
            firstTextColor = colorResource(
                if (isStartDateTimeExist) R.color.light_blue else R.color.gray
            ),
            showIcon = isStartDateTimeExist
        ) {
            isStartDatePickerDialog = false
            showDatePickerDialog.value = true
        }

        TextRowWithDropdownMenu(text = stringResource(R.string.payment))

        TextRow(
            valueDescription = stringResource(R.string.duration),
            value = tripDuration,
            firstTextColor = if (isStartDateTimeExist && isEndDateTimeExist) {
                colorResource(R.color.light_blue)
            } else colorResource(R.color.gray)
        )

        TextRow(
            valueDescription = stringResource(R.string.earnings),
            value = tripEarnings,
            firstTextColor = if (isStartDateTimeExist && isEndDateTimeExist) {
                colorResource(R.color.light_blue)
            } else colorResource(R.color.gray)
        )

        Spacer(modifier = Modifier.height(40.dp))

        AppButton(
            buttonText = stringResource(
                id = if (isNewTrip) R.string.add_trip else R.string.update_trip
            ),
            enabled = isStartDateTimeExist && !isEndDateTimeExist ||
                    isStartDateTimeExist && (trip.startTime!! < trip.endTime!!)
        ) {
            if (isNewTrip) viewModel.addTrip(trip) else viewModel.updateTrip(trip)
            viewModel.showTripContent(false)
        }

        if (isEndDateTimeExist && isEndBeforeStartOrEqual) {
            Row {
                Text(
                    text = stringResource(R.string.start_must_be_before_end),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        DateTimeDialog(
            showDialog = showDatePickerDialog,
            dateTime = if (isStartDatePickerDialog) startDateTime else endDateTime,
            minDate = if (!isStartDatePickerDialog) {
                startDateTime.value?.minusDays(1L)?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            } else null
        ) {
            viewModel.updateCurrentTrip(
                if (isStartDatePickerDialog) {
                    trip.copy(
                        startTime = Date.from(
                            startDateTime.value?.atZone(ZoneId.systemDefault())?.toInstant()
                        )
                    )
                } else {
                    trip.copy(
                        endTime = Date.from(
                            endDateTime.value?.atZone(ZoneId.systemDefault())?.toInstant()
                        )
                    )
                }
            )
        }
    }
}