package com.example.toolsfordriver.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.utils.dateAsStringIso
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLocationDialog(
    isLoadDialog: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    showDateTimeDialog: MutableState<Boolean>,
    location: MutableState<String>,
    dateTime: MutableState<LocalDateTime?>,
    freight: MutableState<FreightDBModel>,
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

        val initDate = rememberSaveable {
            mutableStateOf(
                if (dateTime.value != null) {
                    dateTime.value!!.toInstant(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds()
                } else null
            )
        }

        val currentDate = Clock.System.now().toEpochMilliseconds()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initDate.value ?: currentDate,
            initialDisplayedMonthMillis = initDate.value ?: currentDate
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
                    DialogTitle(
                        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp),
                        title = "Select location, date and time"
                    )

                    LocationContent(
                        countryCode = countryCode,
                        city = city
                    )

                    DateTimePickersContent(
                        showDialog = showDateTimeDialog,
                        datePickerState = datePickerState,
                        timePickerState = timePickerState
                    )

                    DialogButtonsContent(
                        isLoadDialog = isLoadDialog,
                        showDialog = showDialog,
                        freight = freight,
                        location = location,
                        countryCode = countryCode,
                        city = city,
                        initDate = initDate,
                        dateTime = dateTime,
                        datePickerState = datePickerState,
                        timePickerState = timePickerState,
                        context = context
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogButtonsContent(
    isLoadDialog: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    freight: MutableState<FreightDBModel>,
    location: MutableState<String>,
    countryCode: MutableState<String>,
    city: MutableState<String>,
    initDate: MutableState<Long?>,
    dateTime: MutableState<LocalDateTime?>,
    datePickerState: DatePickerState,
    timePickerState: TimePickerState,
    context: Context
) {
    DialogButtons(
        showDialog = showDialog
    ) {
        if (countryCode.value.isNotEmpty() && city.value.isNotEmpty()) {
            showDialog.value = false

            dateTime.value = LocalDateTime(
                LocalDate.parse(
                    dateAsStringIso(datePickerState.selectedDateMillis!!)
                ),
                LocalTime(timePickerState.hour, timePickerState.minute)
            )

            location.value = "${countryCode.value.trim()}#${city.value.trim()}"
            val time = dateTime.value!!
                .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            val loadsUnloads = (if (isLoadDialog.value) {
                freight.value.loads
            } else freight.value.unloads).toMutableMap()

            if (initDate.value != null) {
                if (loadsUnloads.contains(initDate.value)) {
                    loadsUnloads.remove(initDate.value)
                }
            }
            loadsUnloads[time] = location.value

            freight.value = freight.value.copy(
                loads = if (isLoadDialog.value) {
                    loadsUnloads.toMap()
                } else freight.value.loads,
                unloads = if (!isLoadDialog.value) {
                    loadsUnloads.toMap()
                } else freight.value.unloads
            )
        } else {
            Toast.makeText(
                context,
                "Not enough data",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
fun LocationContent(
    countryCode: MutableState<String>,
    city: MutableState<String>
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 0.dp, end = 10.dp, bottom = 0.dp)
    ) {

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val cityNameFocusRequester = remember { FocusRequester() }

        LocationPickerRow(
            description = "Country",
            placeholderText = "Code",
            inputText = countryCode,
            modifier = Modifier.width(150.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Next
            )
        ) {
            keyboardController?.hide()
            cityNameFocusRequester.requestFocus()
        }

        LocationPickerRow(
            description = "City",
            placeholderText = "Name",
            inputText = city,
            isSingleLine = false,
            maxLines = 2,
            modifier = Modifier.focusable(true).focusRequester(cityNameFocusRequester),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }
}

@Composable
fun LocationPickerRow(
    description: String,
    placeholderText: String,
    inputText: MutableState<String>,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
    onAction: () -> Unit
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
            placeholder = {
                Text(
                    text = placeholderText,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            isSingleLine = isSingleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions
        ) {
            onAction.invoke()
        }
    }
}

@Composable
fun LocationInputText(
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit,
    isSingleLine: Boolean,
    maxLines: Int,
    keyboardOptions: KeyboardOptions,
    onAction: () -> Unit
) {

    val trailingIconVisibility = remember { mutableStateOf(true) }

    InputField(
        modifier = modifier.wrapContentWidth(align = Alignment.End),
        textValueState = text,
        placeholder = placeholder,
        isOutlined = false,
        enabled = true,
        isSingleLine = isSingleLine,
        maxLines = maxLines,
        inputTextAlign = TextAlign.End,
        trailingIconVisibility = trailingIconVisibility,
        keyboardOptions = keyboardOptions,
        onAction = onAction
    )
}
