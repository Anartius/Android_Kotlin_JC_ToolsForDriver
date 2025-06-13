package com.example.toolsfordriver.ui.screens.tripsreport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.durationAsString
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.textfields.CenteredTextRow
import com.example.toolsfordriver.ui.common.textfields.HeaderRow
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@Composable
fun TripsReportScreen(
    range: String = "",
    onNavIconClicked: () -> Unit
) {
    val timeZone = ZoneId.systemDefault()
    val viewModel: TripsReportViewModel = hiltViewModel()
    val users = viewModel.users.collectAsStateWithLifecycle(emptyList()).value
    val user = if (users.isNotEmpty()) users.first() else null

    val startOfRange = LocalDate.parse(range.substringBefore(", ")).atStartOfDay(timeZone)
    val startDate = Date.from(startOfRange.toInstant())

    val endOfRange = LocalDate.parse(range.substringAfter(", "))
        .atTime(LocalTime.MAX).atZone(timeZone)
    val endDate = Date.from(endOfRange.toInstant())

    if (startDate != null && endDate != null && user != null) {
        val tripList = viewModel.trips.collectAsStateWithLifecycle(emptyList()).value
        val trips = tripList.filter {
            if (it.startTime != null && it.endTime != null) {
                it.startTime >= startDate && it.startTime < endDate
            } else false
        }
        var dayPaymentDuration by remember { mutableStateOf(Duration.ZERO) }
        var hourPaymentDuration by remember { mutableStateOf(Duration.ZERO) }

        var earnings by remember { mutableDoubleStateOf(0.0) }

        LaunchedEffect(trips) {
            dayPaymentDuration =
                viewModel.calcDayPaymentDuration(trips, timeZone, user.roundUpFromMinutes)

            hourPaymentDuration =
                viewModel.calcHourPaymentDuration(trips, timeZone, user.roundUpFromMinutes)

            earnings = viewModel.calcEarnings(dayPaymentDuration, hourPaymentDuration, user)
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

            Surface(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValue)
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderRow(
                        text = "${dateAsString(startDate)} - ${dateAsString(endDate)}"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        thickness = 0.5.dp,
                        color = colorResource(R.color.light_blue)
                    )
                    CenteredTextRow(
                        text = "Duration with payment per day",
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    )

                    Text(durationAsString(dayPaymentDuration))

                    CenteredTextRow(
                        text = "Duration with payment per hour",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )

                    Text(durationAsString(hourPaymentDuration))

                    CenteredTextRow(
                        text = "Earnings",
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 44.dp, bottom = 24.dp)
                    )

                    Text(
                        text = "$earnings PLN",
                        fontSize = 24.sp,
                        color = Color.Green
                    )
                }
            }
        }
    }
}