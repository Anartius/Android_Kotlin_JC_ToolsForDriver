package com.example.toolsfordriver.ui.screens.tripsreport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.getRangeAsString
import com.example.toolsfordriver.common.getSpecificMonthRange
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.buttons.AppButton
import com.example.toolsfordriver.ui.common.dialogs.DateRangePickerDialog
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TripsReportMenuScreen(
    onNavigateToHomeScreen: () -> Unit = {},
    onNavigateToTripsReportScreen: (String) -> Unit = {}
) {
    val viewModel: TripsReportViewModel = hiltViewModel()
    viewModel.trips.collectAsStateWithLifecycle(emptyList())
    viewModel.users.collectAsStateWithLifecycle(emptyList())

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val showDateRangePicker = uiState.showDateRangePicker

    val currentDate = LocalDate.now()
    val currentYearMonth = YearMonth.from(currentDate)
    val previousYearMonth = currentYearMonth.minusMonths(1)

    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(R.string.trips_report),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = { onNavigateToHomeScreen() }
            )
        }
    ) { paddingValue ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(R.string.current_month)
                ) {
                    onNavigateToTripsReportScreen(getSpecificMonthRange(currentYearMonth))
                }

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(R.string.previous_month)
                ) {
                    onNavigateToTripsReportScreen(getSpecificMonthRange(previousYearMonth))
                }

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(id = R.string.select_period)
                ) {
                    viewModel.showDateRangePicker(true)
                }

                if (showDateRangePicker) {
                    DateRangePickerDialog(
                        onConfirmButtonClicked = { start, end ->
                            viewModel.showDateRangePicker(false)
                            onNavigateToTripsReportScreen(getRangeAsString(start, end))
                        },
                        hideDialog = { viewModel.showDateRangePicker(false) }
                    )
                }
            }
        }
    }
}