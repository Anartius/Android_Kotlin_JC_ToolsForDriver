package com.example.toolsfordriver.ui.screens.trip.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripRow(
    trip: Trip,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        border = BorderStroke(
            width = 0.5.dp,
            color = colorResource(id = R.color.light_blue).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column (
                modifier = Modifier.weight(1f),
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val endDateAsString = dateAsString(trip.endTime)
                val endTimeAsString = timeAsString(trip.endTime)
                Text(
                    text = endDateAsString.ifEmpty { "––.––.––––" }
                )
                Text(
                    text = endTimeAsString.ifEmpty { "––:––" },
                    color = colorResource(id = R.color.gray)
                )
            }
        }
    }
}