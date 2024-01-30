package com.example.toolsfordriver.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.toolsfordriver.R
import com.example.toolsfordriver.utils.dateAsString

@Composable
fun DialogTitle(title: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)
    ) {
        Text(text = title, color = colorResource(id = R.color.light_blue))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickersContent(
    showDialog: MutableState<Boolean>,
    datePickerState: DatePickerState,
    timePickerState: TimePickerState
) {
    Column(modifier = Modifier.padding(vertical = 0.dp, horizontal = 20.dp)) {
        DatePickerRow(
            datePickerState = datePickerState,
            showDialog = showDialog
        )
        TimePickerRow(timePickerState = timePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerRow(
    datePickerState: DatePickerState,
    showDialog: MutableState<Boolean>
) {
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth()
            .clickable { showDatePickerDialog = true },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var dateRowText by rememberSaveable { mutableStateOf("Select Date") }

        Text(text = "Date:")
        Row {
            Text(text = dateRowText, modifier = Modifier.padding(end = 5.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "pick date",
                tint = colorResource(id = R.color.light_blue)
            )
        }


        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = {showDialog.value = false},
                confirmButton = {
                    TextButton(onClick = {
                        showDatePickerDialog = false
                        dateRowText = dateAsString(datePickerState.selectedDateMillis!!)
                    }) {
                        Text(text = "Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerRow(timePickerState: TimePickerState) {
    val showTimePickerDialog = rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth()
            .clickable { showTimePickerDialog.value = true },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val timeRowText = rememberSaveable { mutableStateOf("Select Time") }

        Text(text = "Time:")
        Row {
            Text(text = timeRowText.value, modifier = Modifier.padding(end = 5.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "pick time",
                tint = colorResource(id = R.color.light_blue)
            )
        }

        if (showTimePickerDialog.value) {
            AppTimePickerDialog(
                showTimePickerDialog = showTimePickerDialog,
                timePickerState = timePickerState,
                timeRowText = timeRowText
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    showTimePickerDialog: MutableState<Boolean>,
    timePickerState: TimePickerState,
    timeRowText: MutableState<String>
) {
    Dialog(onDismissRequest = { showTimePickerDialog.value = false }) {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val colorLightBlue = colorResource(id = R.color.light_blue)
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TimePickerDefaults.colors(
                        selectorColor = colorLightBlue,
                        timeSelectorSelectedContainerColor = colorLightBlue
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        showTimePickerDialog.value = false
                    }) {
                        Text(text = "Cancel", color = colorLightBlue)
                    }
                    TextButton(onClick = {
                        showTimePickerDialog.value = false
                        val hour = timePickerState.hour
                        val minute = timePickerState.minute
                        timeRowText.value = String.format("%02d:%02d", hour, minute)
                    }) {
                        Text(text = "Ok", color = colorResource(id = R.color.light_blue))
                    }
                }
            }
        }
    }
}

@Composable
fun DialogButtons(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit = {}
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        AppButton(
            buttonText = "Cancel",
            modifier = Modifier.padding(10.dp)
        ) {
            showDialog.value = false
        }
        AppButton(
            buttonText = "Save",
            modifier = Modifier.padding(10.dp)
        ) { onConfirm.invoke() }
    }
}