package com.example.toolsfordriver.ui.common.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.toolsfordriver.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeDialog (
    showDialog: MutableState<Boolean>,
    dateTime: MutableState<LocalDateTime?>,
    minDate: Long? = null,
    onConfirmButtonClicked: () -> Unit = {}
) {
    if (showDialog.value) {

        val initDate = if (dateTime.value != null) {
            dateTime.value!!.toInstant(ZoneOffset.UTC).toEpochMilli()
        } else LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initDate,
            selectableDates = if (minDate != null) {
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis >= minDate
                    }
                }
            } else DatePickerDefaults.AllDates
        )

        val currentTime = LocalDateTime.now()
        val timePickerState = rememberTimePickerState(
            initialHour = dateTime.value?.hour ?: currentTime.hour,
            initialMinute = dateTime.value?.minute ?: currentTime.minute
        )

        Dialog(onDismissRequest = {}) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.dark_gray).copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 0.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DialogTitle(title = stringResource(id = R.string.select_date_and_time))

                    DatePickerRow(datePickerState = datePickerState)

                    TimePickerRow(timePickerState = timePickerState)

                    DialogButtons(
                        onConfirm = {
                            if (datePickerState.selectedDateMillis != null) {
                                dateTime.value = LocalDateTime.of(
                                    Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                                        .atZone(ZoneId.systemDefault()).toLocalDate(),
                                    LocalTime.of(timePickerState.hour, timePickerState.minute)
                                        .atOffset(ZoneOffset.UTC).toLocalTime()
                                )
                            }

                            onConfirmButtonClicked()
                            showDialog.value = false
                        },
                        onDismiss = { showDialog.value = false }
                    )
                }
            }
        }
    }
}

