package com.example.toolsfordriver.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
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
fun LocationDateTimeDialog(
    showDialog: MutableState<Boolean>,
    showDateTimeDialog: MutableState<Boolean>,
    dateTime: MutableState<LocalDateTime?>,
    location: MutableState<String>,
    context: Context
) {
    if (showDialog.value) {
        val countryCode = rememberSaveable {
            mutableStateOf(
                if (location.value.contains('#')) {
                    location.value.split("#").first()
                } else  ""
            )
        }
        val city = rememberSaveable {
            mutableStateOf(
                if (location.value.contains('#')) {
                    location.value.split("#").last()
                } else  ""
            )
        }

        val initDate by rememberSaveable {
            mutableStateOf(
                if (dateTime.value != null) {
                    dateTime.value!!.toInstant(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds()
                } else null
            )
        }

        val currentDate = Clock.System.now().toEpochMilliseconds()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initDate ?: currentDate,
            initialDisplayedMonthMillis = initDate ?: currentDate
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
                    DialogTitle(title = "Select location, date and time")

                    LocationContent(
                        countryCode = countryCode,
                        city = city
                    )

                    DateTimePickersContent(
                        showDialog = showDateTimeDialog,
                        datePickerState = datePickerState,
                        timePickerState = timePickerState
                    )

                    DialogButtons(
                        showDialog = showDialog
                    ) {
                        if (countryCode.value.isNotEmpty() && city.value.isNotEmpty()) {
                            showDialog.value = false

                            dateTime.value = LocalDateTime(
                                LocalDate.parse(
                                    dateAsString(datePickerState.selectedDateMillis!!)
                                ),
                                LocalTime(timePickerState.hour, timePickerState.minute)
                            )

                            location.value = "${countryCode.value.trim()}#${city.value.trim()}"
                        } else {
                            Toast.makeText(
                                context,
                                "There doesn't seem to be enough data.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationContent(
    countryCode: MutableState<String>,
    city: MutableState<String>,
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 0.dp, end = 10.dp, bottom = 0.dp)
    ) {
        LocationPickerRow(
            description = "Country",
            label = "Code",
            inputText = countryCode,
            modifier = Modifier.width(150.dp),
            keyboardOptions =
            KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
        )
        LocationPickerRow(
            description = "City",
            label = "Name",
            inputText = city,
            modifier = Modifier,
            isSingleLine = false,
            maxLines = 2
        )
    }
}

@Composable
fun LocationPickerRow(
    description: String,
    label: String,
    inputText: MutableState<String>,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "$description:")

        LocationInputText(
            text = inputText,
            modifier = modifier,
            label = label,
            isSingleLine = isSingleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions
        )
    }
}

@Composable
fun LocationInputText(
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    label: String,
    isSingleLine: Boolean,
    maxLines: Int,
    keyboardOptions: KeyboardOptions,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val trailingIconVisibility = remember { mutableStateOf(true) }

    InputField(
        modifier = modifier,
        valueState = text,
        label = label,
        isOutlined = false,
        enabled = true,
        isSingleLine = isSingleLine,
        maxLines = maxLines,
        trailingIconVisibility = trailingIconVisibility,
        keyboardOptions = keyboardOptions,
        onAction =  KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hide()
            }
        )
    )
}
