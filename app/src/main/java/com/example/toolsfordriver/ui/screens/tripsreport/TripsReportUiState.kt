package com.example.toolsfordriver.ui.screens.tripsreport

import java.time.Duration


data class TripsReportUiState(
    val dailyPaymentDuration: Duration = Duration.ZERO,
    val hourlyPaymentDuration: Duration = Duration.ZERO,
    val earnings: Double = 0.0,
    val showDateRangePicker: Boolean = false
)