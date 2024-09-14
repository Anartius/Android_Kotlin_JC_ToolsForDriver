package com.example.toolsfordriver.ui.screens.trip.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import com.example.toolsfordriver.common.dateAsString
import com.example.toolsfordriver.common.timeAsString
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.ui.screens.trip.TripViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripRow(
    trip: Trip,
    viewModel: TripViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .combinedClickable(
                enabled = true,
                onClick = {
                    viewModel.updateCurrentTrip(trip)
                    viewModel.setCurrentTripAsNew(false)
                    viewModel.showTripContent(true)
                },
                onLongClick = {
                    viewModel.addTripToDelete(trip)
                    viewModel.showDeletePopup(true)
                }
            ),
        border = BorderStroke(
            width = 0.5.dp,
            color = colorResource(id = R.color.light_blue).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,

            ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateAsString(trip.startTime))
                Text(
                    text = timeAsString(trip.startTime),
                    color = colorResource(id = R.color.gray)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(id = R.string.arrow_forward),
                tint = colorResource(id = R.color.gray),
            )
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateAsString(trip.endTime))
                Text(
                    text = timeAsString(trip.endTime),
                    color = colorResource(id = R.color.gray)
                )
            }
        }
    }
}