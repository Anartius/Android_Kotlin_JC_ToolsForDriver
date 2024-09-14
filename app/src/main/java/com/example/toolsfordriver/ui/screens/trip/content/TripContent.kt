package com.example.toolsfordriver.ui.screens.trip.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.screens.trip.TripViewModel

@Composable
fun TripContent(
    viewModel: TripViewModel,
    onNavIconClicked: () -> Unit
) {
    BackHandler {
        viewModel.showTripContent(false)
    }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(id = R.string.trip),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = { onNavIconClicked() }
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
                    .padding(vertical = 25.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                TripContentColumn(viewModel = viewModel)
            }
        }
    }
}