package com.example.toolsfordriver.ui.screens.trip.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.toolsfordriver.R
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.ui.screens.trip.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextRowWithDropdownMenu(
    text: String,
    trip: Trip
) {
    val viewModel: TripViewModel = hiltViewModel()
    val items = listOf(stringResource(R.string.per_day), stringResource(R.string.per_hour))
    val selectedOptionText = if (trip.hourlyPayment) items[1] else items[0]
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.light_blue)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier.menuAnchor(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedOptionText, modifier = Modifier.padding(horizontal = 10.dp))

                Icon(
                    imageVector = if (expanded) {
                        Icons.Filled.ArrowDropUp
                    } else Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.select),
                    tint = colorResource(id = R.color.light_blue),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        border = BorderStroke(1.dp, colorResource(id = R.color.light_blue)),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                items.forEach { stringValue ->
                    DropdownMenuItem(
                        text = {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(text = stringValue)

                                if (stringValue == selectedOptionText) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = stringResource(
                                            R.string.selected_option
                                        ),
                                        tint = colorResource(id = R.color.light_blue)
                                    )
                                }
                            }
                        },
                        onClick = {
                            expanded = false
                            viewModel.updateCurrentTrip(
                                trip.copy(hourlyPayment = stringValue == items[1])
                            )
                        },
                        modifier = Modifier.background(Color.Transparent)
                    )
                }
            }
        }
    }
}