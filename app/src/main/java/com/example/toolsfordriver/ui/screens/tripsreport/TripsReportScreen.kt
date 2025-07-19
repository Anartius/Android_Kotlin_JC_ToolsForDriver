package com.example.toolsfordriver.ui.screens.tripsreport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.durationAsString
import com.example.toolsfordriver.common.getRangeAsString
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.TextRow
import com.example.toolsfordriver.ui.common.dialogs.DateRangePickerDialog
import com.example.toolsfordriver.ui.common.text.HeaderRow
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@Composable
fun TripsReportScreen(
    rangeValue: String,
    onNavIconClicked: () -> Unit
) {
    val timeZone = ZoneId.systemDefault()
    val screenHeight = LocalWindowInfo.current.containerSize.height
    val viewModel: TripsReportViewModel = hiltViewModel()
    val users = viewModel.users.collectAsStateWithLifecycle(emptyList()).value
    val user = if (users.isNotEmpty()) users.first() else null

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val showDateRangePicker = uiState.showDateRangePicker
    val dailyPayDuration = uiState.dailyPaymentDuration
    val hourlyPayDuration = uiState.hourlyPaymentDuration
    val earnings = uiState.earnings

    var range by remember { mutableStateOf(rangeValue) }

    val startLocalDate = LocalDate.parse(range.substringBefore(", "))
    val startDate = Date.from(startLocalDate.atStartOfDay(timeZone).toInstant())

    val endLocalDate = LocalDate.parse(range.substringAfter(", "))
    val endDate =
        Date.from(endLocalDate.atTime(LocalTime.MAX).atZone(timeZone).toInstant())

    if (startDate != null && endDate != null && user != null) {
        val tripList = viewModel.trips.collectAsStateWithLifecycle(emptyList()).value
        val tripsMap = viewModel.getMappedTrips(
            tripList, startDate, endDate, timeZone, user)
        val dailyTrips = tripsMap["d"]
        val hourlyTrips = tripsMap["h"]


        LaunchedEffect(tripList, range) {
            dailyTrips?.let { viewModel.updateDailyPayData(it, user) }
            hourlyTrips?.let { viewModel.updateHourlyPayData(it, user) }
        }

        Scaffold(
            topBar = {
                TFDAppBar(
                    title = stringResource(R.string.trips_report),
                    navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    onNavIconClicked = { onNavIconClicked() }
                )
            }
        ) { paddingValue ->
            Surface(modifier = Modifier.fillMaxSize().padding(paddingValue)) {
                Column(
                    modifier = Modifier.padding(bottom = 24.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    HeaderRow(
                        text = "${dateAsString(startDate)} - ${dateAsString(endDate)}",
                        onClick = { viewModel.showDateRangePicker(true) }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        thickness = 0.5.dp,
                        color = colorResource(R.color.light_blue)
                    )

                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                            .heightIn(max = (screenHeight - 330).dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (dailyPayDuration != Duration.ZERO) {
                            TripSReportDurationItem(
                                title = stringResource(R.string.payment_per_day),
                                value = durationAsString(dailyPayDuration),
                                tripList = dailyTrips
                            )
                        }

                        if (hourlyPayDuration != Duration.ZERO) {
                            TripSReportDurationItem(
                                title = stringResource(R.string.payment_per_hour),
                                value = durationAsString(hourlyPayDuration),
                                tripList = hourlyTrips,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
                        thickness = 0.5.dp,
                        color = colorResource(R.color.light_blue)
                    )

                    TextRow(
                        valueDescription = stringResource(R.string.earnings),
                        value = "$earnings PLN",
                        fontSize = 20.sp
                    )
                }

                if (showDateRangePicker) {
                    val pickerStartDate = Date
                        .from(
                            startLocalDate.atStartOfDay(ZoneId.of("UTC")).toInstant()
                        ).time

                    val pickerEndDate = Date
                        .from(
                            endLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"))
                                .toInstant()
                        ).time

                    DateRangePickerDialog(
                        initialStartDate = pickerStartDate,
                        initialEndDate = pickerEndDate,
                        onConfirmButtonClicked = { start, end ->
                            viewModel.showDateRangePicker(false)
                            range = getRangeAsString(start, end)
                        },
                        hideDialog = { viewModel.showDateRangePicker(false) }
                    )
                }
            }
        }
    }
}