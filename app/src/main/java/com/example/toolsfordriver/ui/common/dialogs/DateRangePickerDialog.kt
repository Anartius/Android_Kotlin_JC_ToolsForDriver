package com.example.toolsfordriver.ui.common.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.TFDLocaleManager
import com.example.toolsfordriver.common.dateAsString
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    initialStartDate: Long? = null,
    initialEndDate: Long? = null,
    onConfirmButtonClicked: (Long, Long) -> Unit,
    hideDialog: () -> Unit
) {
    val dateRangeState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartDate,
        initialSelectedEndDateMillis = initialEndDate
    )

    val locale = TFDLocaleManager.getSavedLocale(LocalContext.current)
    val config = Configuration().apply {
        updateFrom(LocalConfiguration.current)

        ConfigurationCompat.setLocales(
            this,
            LocaleListCompat.create(
                Locale.Builder()
                    .setRegion("PL")
                    .setLanguage(locale.language)
                    .setUnicodeLocaleKeyword("fw", "mon")
                    .build()
            )
        )
    }
    val newContext = LocalContext.current.createConfigurationContext(config)

    var isPeriodSelected by remember(
        dateRangeState.selectedStartDateMillis, dateRangeState.selectedEndDateMillis
    ) {
        mutableStateOf(dateRangeState.selectedStartDateMillis != null
                && dateRangeState.selectedEndDateMillis != null)
    }

    DatePickerDialog(
        onDismissRequest = { hideDialog() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButtonClicked(
                        dateRangeState.selectedStartDateMillis!!,
                        dateRangeState.selectedEndDateMillis!!
                    )

                    dateRangeState.setSelection(startDateMillis = null, endDateMillis = null)
                    hideDialog()
                },
                enabled = isPeriodSelected
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    color = colorResource(
                        id = if (isPeriodSelected) R.color.light_blue else R.color.gray
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { hideDialog() }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = colorResource(id = R.color.light_blue)
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = colorResource(id = R.color.dark_gray).copy(alpha = 0.8f),
        )
    ) {

        CompositionLocalProvider(
            LocalContext provides newContext,
            LocalConfiguration provides config
        ) {
            DateRangePicker(
                state = dateRangeState,
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.select_period),
                            fontSize = 20.sp,
                            color = colorResource(R.color.light_blue)
                        )
                    }
                },
                headline = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f)) {
                            Text(
                                text = dateAsString(dateRangeState.selectedStartDateMillis),
                                fontSize = 18.sp,
                                color = colorResource(R.color.light_blue)
                            )
                        }

                        Box {
                            Text(
                                text = "-",
                                fontSize = 18.sp,
                                color = colorResource(R.color.light_blue)
                            )
                        }

                        Box(modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f)) {
                            Text(
                                text = dateAsString(dateRangeState.selectedEndDateMillis),
                                fontSize = 18.sp,
                                color = colorResource(R.color.light_blue)
                            )
                        }
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Transparent,
                    subheadContentColor = colorResource(id = R.color.light_blue),
                    headlineContentColor = colorResource(id = R.color.light_blue),
                    selectedDayContainerColor = colorResource(id = R.color.light_blue),
                    dayInSelectionRangeContainerColor =
                    colorResource(id = R.color.light_blue).copy(alpha = 0.4f),
                    todayContentColor = colorResource(id = R.color.light_blue),
                    todayDateBorderColor = colorResource(id = R.color.light_blue),
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedIndicatorColor = colorResource(id = R.color.light_blue),
                        focusedLabelColor = colorResource(id = R.color.light_blue),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        cursorColor = colorResource(id = R.color.light_blue),
                        selectionColors = TextSelectionColors(
                            handleColor = colorResource(id = R.color.light_blue),
                            backgroundColor = Color.Transparent
                        )
                    )
                )
            )
        }
    }
}