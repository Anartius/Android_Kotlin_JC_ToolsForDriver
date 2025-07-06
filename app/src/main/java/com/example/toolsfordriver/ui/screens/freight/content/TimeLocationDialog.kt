package com.example.toolsfordriver.ui.screens.freight.content

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.InputField
import com.example.toolsfordriver.ui.common.dialogs.DatePickerRow
import com.example.toolsfordriver.ui.common.dialogs.DialogButtons
import com.example.toolsfordriver.ui.common.dialogs.DialogTitle
import com.example.toolsfordriver.ui.common.dialogs.TimePickerRow
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLocationDialog(
    isLoadDialog: Boolean,
    location: MutableState<String>,
    dateTime: ZonedDateTime?,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val zoneId = ZoneId.systemDefault()
    val viewModel: FreightViewModel = hiltViewModel()
    val freight = viewModel.uiState.collectAsStateWithLifecycle().value.currentFreight!!

    val dateTimeMillis = dateTime?.toInstant()?.toEpochMilli()

    val currentDateTime = LocalDateTime.now()
    val currentDate = currentDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateTimeMillis ?: currentDate,
        initialDisplayedMonthMillis = dateTimeMillis ?: currentDate
    )

    val timePickerState = rememberTimePickerState(
        initialHour = dateTime?.hour ?: currentDateTime.hour,
        initialMinute = dateTime?.minute ?: currentDateTime.minute
    )

    var selectedDateTime by rememberSaveable { mutableStateOf<ZonedDateTime?>(null)}

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

    Dialog(onDismissRequest = {}) {

        LaunchedEffect(
            datePickerState.selectedDateMillis,
            timePickerState.minute,
            timePickerState.hour
        ) {
            val timeZone = ZoneId.systemDefault()

            selectedDateTime = ZonedDateTime.of(
                    Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                        .atZone(zoneId).toLocalDate(),
                    LocalTime.of(timePickerState.hour, timePickerState.minute),
                    timeZone
            )
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.dark_gray).copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DialogTitle(title = stringResource(R.string.select_location_date_and_time))

                LocationContent(
                    countryCode = countryCode,
                    city = city
                )

                Column(modifier = Modifier.padding(vertical = 0.dp, horizontal = 20.dp)) {
                    DatePickerRow(datePickerState = datePickerState)
                    TimePickerRow(timePickerState = timePickerState)
                }

                DialogButtons(
                    confirmButtonIsEnabled = countryCode.value.isNotEmpty() &&
                            city.value.isNotEmpty() && selectedDateTime != null,
                    onConfirm = {
                        val location = "${countryCode.value.trim()}#${city.value.trim()}"

                        val loadsUnloads =
                            (if (isLoadDialog) freight.loads else freight.unloads).toMutableMap()

                        if (dateTimeMillis != null) {
                            if (loadsUnloads.contains(dateTime.toString())) {
                                loadsUnloads.remove(dateTime.toString())
                            }
                        }

                        loadsUnloads[selectedDateTime.toString()] = location

                        val loads = if (isLoadDialog) loadsUnloads  else freight.loads
                        val unloads = if(!isLoadDialog) loadsUnloads else freight.unloads

                        var firstLoadDateTime: Date? = null
                        var lastUnloadDateTime: Date? = null

                        if (isLoadDialog && loads.isNotEmpty()) {
                            firstLoadDateTime = Date.from(
                                ZonedDateTime.parse(loads.keys.min()).toInstant()
                            )
                        }
                        if (!isLoadDialog && unloads.isNotEmpty()) {
                            lastUnloadDateTime = Date.from(
                                ZonedDateTime.parse(unloads.keys.max()).toInstant()
                            )
                        }

                        viewModel.updateCurrentFreight(
                            freight.copy(
                                loads = loads,
                                unloads = unloads,
                                firstLoadTime = firstLoadDateTime,
                                lastUnloadTime = lastUnloadDateTime
                            )
                        )

                        onConfirm()
                    },
                    onDismiss = onDismiss
                )
            }
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
            description = stringResource(R.string.country),
            placeholderText = stringResource(R.string.code),
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
            description = stringResource(R.string.city),
            placeholderText = stringResource(R.string.name),
            inputText = city,
            isSingleLine = false,
            maxLines = 2,
            modifier = Modifier
                .focusable(true)
                .focusRequester(cityNameFocusRequester),
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

        InputField(
            modifier = modifier.wrapContentWidth(align = Alignment.End),
            textValueState = inputText,
            placeholder = {
                Text(
                    text = placeholderText,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            isOutlined = false,
            enabled = true,
            isSingleLine = isSingleLine,
            maxLines = maxLines,
            inputTextAlign = TextAlign.End,
            trailingIconVisibility = true,
            keyboardOptions = keyboardOptions
        ) { onAction() }
    }
}