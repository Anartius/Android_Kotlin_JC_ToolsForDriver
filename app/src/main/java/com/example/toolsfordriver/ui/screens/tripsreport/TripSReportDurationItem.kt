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
import com.composables.icons.lucide.ChevronsDownUp
import com.composables.icons.lucide.ChevronsUpDown
import com.composables.icons.lucide.Lucide
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.durationAsString
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.ui.common.text.AutoResizeableText
import com.example.toolsfordriver.ui.common.text.TitleRowWithIcons
import com.example.toolsfordriver.ui.common.text.TitleTextRow
import java.time.Duration

@Composable
fun TripSReportDurationItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    tripList: List<Pair<Trip, Duration?>>? = null
) {
    var showTripList by remember { mutableStateOf(true) }
    var showEveryTripDuration by remember { mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)) {
        TitleRowWithIcons(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            primIcon = if (showTripList) {
                Icons.Filled.KeyboardArrowDown
            } else Icons.Filled.KeyboardArrowUp,
            showPrimIcon = true,
            primIconDescription = if (showTripList) {
                stringResource(R.string.hide_list_of_trips)
            } else stringResource(R.string.show_list_of_trips),
            onPrimIconClick = { showTripList = !showTripList },
            secIcon = if (showEveryTripDuration) {
                Lucide.ChevronsUpDown
            } else Lucide.ChevronsDownUp,
            showSecIcon = showTripList,
            secIconDescription = if (showEveryTripDuration) {
                stringResource(R.string.hide_every_trip_duration)
            } else stringResource(R.string.show_every_trip_duration),
            secIconSize = 18.dp,
            onSecIconClick = { showEveryTripDuration = !showEveryTripDuration }
        )

        if (showTripList) {
            tripList?.let { trip ->
                trip.forEach {
                    RangeTextRow(it.first)
                    if (showEveryTripDuration) {
                        Text(text = durationAsString(it.second))
                    }
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