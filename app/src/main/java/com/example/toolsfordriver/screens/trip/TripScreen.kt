package com.example.toolsfordriver.screens.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.components.AppButton
import com.example.toolsfordriver.components.DateTimeDialog
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.components.TextRow
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.example.toolsfordriver.utils.calcEarnings
import com.example.toolsfordriver.utils.calcPeriod
import com.example.toolsfordriver.utils.dateAsString
import com.example.toolsfordriver.utils.formatPeriod
import com.example.toolsfordriver.utils.timeAsString
import com.google.firebase.auth.FirebaseAuth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun TripScreen(
    navController: NavController,
    viewModel: TripScreenViewModel,
    tripId: String
) {
    val isCreateTrip = remember(tripId) { mutableStateOf(tripId == "new" ) }

    val tripList = if (!isCreateTrip.value) {
        viewModel.tripList.collectAsState().value
    } else emptyList()

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Trip",
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                navIconDescription = "Back",
                onNavIconClicked = {
                    navController.navigate(TFDScreens.TripListScreen.name)
                }
            )
        }
    ) { paddingValue ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp, vertical = 25.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                if (tripList.isEmpty() && !isCreateTrip.value) {
                    CircularProgressIndicator()
                } else {
                    TripScreenContent(
                        tripList = tripList,
                        isCreateTrip = isCreateTrip,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TripScreenContent(
    tripList: List<TripDBModel>,
    isCreateTrip: MutableState<Boolean>,
    navController: NavController,
    viewModel: TripScreenViewModel
) {
    val showDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    val isStartDatePickerDialog = remember { mutableStateOf(true) }

    val trip = if (tripList.isNotEmpty()) {
        tripList.first()
    } else TripDBModel(userId = FirebaseAuth.getInstance().currentUser!!.uid)

    val startDateTime = remember { mutableStateOf(
        if (trip.startTime != null) {
            LocalDateTime(
                LocalDate.parse(dateAsString(trip.startTime)),
                LocalTime.parse(timeAsString(trip.startTime))
            )
        } else null
    )}
    val endDateTime = remember { mutableStateOf(
        if (trip.endTime != null) {
            LocalDateTime(
                LocalDate.parse(dateAsString(trip.endTime)),
                LocalTime.parse(timeAsString(trip.endTime))
            )
        } else null
    )}

    val tripDuration = remember(startDateTime.value, endDateTime.value) {
        mutableStateOf(calcPeriod(startDateTime.value, endDateTime.value))
    }

    val earnings = remember(startDateTime.value, endDateTime.value) {
        mutableStateOf(
            calcEarnings(startDateTime.value, endDateTime.value, (400.0 / 24))
        )
    }

    TextRow(
        valueDescription = "Start",
        value = "${startDateTime.value?.date ?: ""} ${startDateTime.value?.time ?: ""}",
        clickable = true,
        showIcon = true
    ) {
        isStartDatePickerDialog.value = true
        showDatePickerDialog.value = true
    }
    TextRow(
        valueDescription = "Finish",
        value = "${endDateTime.value?.date ?: ""} ${endDateTime.value?.time ?: ""}",
        clickable = true,
        showIcon = true
    ) {
        isStartDatePickerDialog.value = false
        showDatePickerDialog.value = true
    }

    TextRow(
        valueDescription = "Duration",
        value = if (tripDuration.value != null) {
            formatPeriod(tripDuration.value!!)
        } else ""
    )
    TextRow(
        valueDescription = "Earnings",
        value = if (earnings.value != null) {
            "${earnings.value} PLN"
        } else "You should work more"
    )

    Spacer(modifier = Modifier.height(40.dp))

    AppButton(
        buttonText = if (isCreateTrip.value) "Add Trip" else "Update Trip",
        enabled = ((startDateTime.value != null) xor (endDateTime.value != null)) ||
                startDateTime.value != null && endDateTime.value != null &&
                (startDateTime.value!! < endDateTime.value!!),
    ) {
        val tripUpdated = trip.copy(
            startTime = startDateTime
                .value?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds(),
            endTime = endDateTime
                .value?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
        )
        if (isCreateTrip.value) {
            viewModel.addTrip(tripUpdated)
        } else viewModel.updateTrip(tripUpdated)
        navController.popBackStack()
    }

    DateTimeDialog(
        showDialog = showDatePickerDialog,
        dateTime = if (isStartDatePickerDialog.value) startDateTime else endDateTime
    )
}