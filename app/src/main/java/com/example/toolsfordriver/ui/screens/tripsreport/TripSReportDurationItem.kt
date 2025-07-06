package com.example.toolsfordriver.ui.screens.tripsreport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.ui.common.text.AutoResizeableText
import com.example.toolsfordriver.ui.common.text.TitleRowWithIcon
import com.example.toolsfordriver.ui.common.text.TitleTextRow

@Composable
fun TripSReportDurationItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    tripList: List<Trip>? = null
) {
    var showTripList by remember { mutableStateOf(true) }

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)) {
        TitleRowWithIcon(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            icon = if (showTripList) {
                Icons.Filled.KeyboardArrowDown
            } else Icons.Filled.KeyboardArrowUp,
            showIcon = true,
            iconDescription = if (showTripList) {
                stringResource(R.string.hide_list_of_trips)
            } else stringResource(R.string.show_list_of_trips),
        ) {
            showTripList = !showTripList
        }

        if (showTripList) {
            tripList?.let { trip ->
                trip.forEach {
                    RangeTextRow(it)
                }
            }
        }

        TitleTextRow(
            text = stringResource(R.string.summary),
            modifier = Modifier.padding(top = 8.dp))

        Text(text = value)
    }
}

@Composable
fun RangeTextRow(trip: Trip) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AutoResizeableText(
            text = "${dateAsString(trip.startTime)} " +
                    "${timeAsString(trip.startTime)}  -  " +
                    "${dateAsString(trip.endTime)} " +
                    timeAsString(trip.endTime),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}