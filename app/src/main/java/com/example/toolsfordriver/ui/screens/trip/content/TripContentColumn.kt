package com.example.toolsfordriver.ui.screens.trip.content

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant

@Composable
fun TripContentColumn() {
    val viewModel: TripViewModel = hiltViewModel()
    val timeZone = TimeZone.currentSystemDefault()
    val showDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    var isStartDatePickerDialog by remember { mutableStateOf(true) }

    val trip = viewModel.uiState.collectAsStateWithLifecycle().value.currentTrip
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList()).value

    if (trip != null && users.isNotEmpty()) {
        val user = users.first()
        val isNewTrip = viewModel.uiState.collectAsStateWithLifecycle().value.isNewTrip

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

        var isTripPeriodInsideOneMonth by remember(endDateTime, startDateTime) {
            mutableStateOf(
                isStartDateTimeExist && isEndDateTimeExist &&
                        startDateTime.value?.month == endDateTime.value?.month
            )
        }

        val tripDuration = remember(trip) {
            mutableStateOf(
                calcPeriod(startDateTime.value, endDateTime.value)
            )
        }

        val earnings = remember(trip) {
            mutableStateOf(
                calcEarnings(startDateTime.value, endDateTime.value, (paymentPerHour))
            )
        }

        TextRow(
            valueDescription = stringResource(R.string.start),
            value = "${
                dateAsString(startDateTime.value?.toInstant(timeZone)?.toEpochMilliseconds())
            }  " + "${startDateTime.value?.time ?: ""}",
            clickable = true,
            showIcon = true,
            firstTextColor = colorResource(R.color.light_blue)
        ) {
            isStartDatePickerDialog = true
            showDatePickerDialog.value = true
        }

        TextRow(
            valueDescription = stringResource(R.string.end),
            value = "${
                dateAsString(endDateTime.value?.toInstant(timeZone)?.toEpochMilliseconds())
            }  " + "${endDateTime.value?.time ?: ""}",
            clickable = isStartDateTimeExist,
            firstTextColor = colorResource(
                if (isStartDateTimeExist) R.color.light_blue else R.color.gray
            ),
            showIcon = isStartDateTimeExist
        ) {
            isStartDatePickerDialog = false
            showDatePickerDialog.value = true
        }

        TextRowWithDropdownMenu(
            text = stringResource(R.string.payment),
            trip = trip
        )

        TextRow(
            valueDescription = stringResource(R.string.duration),
            value = if (tripDuration.value != null) formatPeriod(tripDuration.value!!) else "",
            firstTextColor = if (isStartDateTimeExist && isEndDateTimeExist) {
                colorResource(R.color.light_blue)
            } else colorResource(R.color.gray)
        )

        TextRow(
            valueDescription = stringResource(R.string.earnings),
            value = if (earnings.value != null) "${earnings.value} PLN" else "",
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
                    isStartDateTimeExist &&
                    (trip.startTime!! < trip.endTime!!) &&
                    isTripPeriodInsideOneMonth
        ) {
            if (isNewTrip) viewModel.addTrip(trip) else viewModel.updateTrip(trip)

            viewModel.showTripContent(false)
        }

        if (isEndDateTimeExist && (isEndBeforeStartOrEqual || !isTripPeriodInsideOneMonth)) {
            Row {
                Text(
                    text = if (isEndBeforeStartOrEqual) {
                        stringResource(R.string.start_must_be_before_end)
                    } else stringResource(R.string.dates_must_be_in_the_same_month),
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
            selectableTimeRange = if (!isStartDatePickerDialog) {
                getSelectableDateRange(startDateTime.value?.date)
            } else null
        ) {
            viewModel.updateCurrentTrip(
                if (isStartDatePickerDialog) {
                    trip.copy(
                        startTime = startDateTime.value?.toInstant(timeZone)?.toEpochMilliseconds()
                    )
                } else {
                    trip.copy(
                        endTime = endDateTime.value?.toInstant(timeZone)?.toEpochMilliseconds()
                    )
                }
            )
        }
    }
}

fun getSelectableDateRange(startDate: LocalDate?): LongRange? {
    return if (startDate != null) {
        val timeZone = TimeZone.currentSystemDefault()
        val year = startDate.year
        val month = startDate.month

        val nextMonth = if (month == Month.DECEMBER) {
            LocalDate(year +1, 1, 1)
        } else LocalDate(year, month + 1, 1)

        val lastDayOfMonth = nextMonth.minus(DatePeriod(days = 1)).dayOfMonth

        val start = LocalDate(year, month, 1).atStartOfDayIn(timeZone).toEpochMilliseconds()
        val end = LocalDate(year, month, lastDayOfMonth).atStartOfDayIn(timeZone)
            .toEpochMilliseconds()

        start..end
    } else null
}