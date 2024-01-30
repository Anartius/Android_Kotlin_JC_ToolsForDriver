package com.example.toolsfordriver.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.toolsfordriver.utils.dateAsString
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeDialog(
    showDialog: MutableState<Boolean>,
    dateTime: MutableState<LocalDateTime?>
) {
    if (showDialog.value) {

        val initDate = if (dateTime.value != null) {
            dateTime.value!!.toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        } else null
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initDate ?: 0L,
            initialDisplayedMonthMillis =
            initDate ?: Clock.System.now().toEpochMilliseconds()
        )

        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val timePickerState = rememberTimePickerState(
            initialHour = dateTime.value?.hour ?: currentTime.hour,
            initialMinute = dateTime.value?.minute ?: currentTime.minute
        )

        Dialog(onDismissRequest = {}) {
            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DialogTitle(title = "Select date and time")

                    DateTimePickersContent(
                        showDialog = showDialog,
                        datePickerState = datePickerState,
                        timePickerState = timePickerState
                    )

                    DialogButtons(
                        showDialog = showDialog
                    ) {
                        showDialog.value = false
                        if (datePickerState.selectedDateMillis != null) {
                            dateTime.value = LocalDateTime(
                                LocalDate.parse(dateAsString(datePickerState.selectedDateMillis!!)),
                                LocalTime(timePickerState.hour, timePickerState.minute)
                            )
                        }
                    }
                }
            }
        }
    }
}

