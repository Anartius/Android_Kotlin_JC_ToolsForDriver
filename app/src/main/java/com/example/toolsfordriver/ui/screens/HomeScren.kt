package com.example.toolsfordriver.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.components.AppButton
import com.example.toolsfordriver.ui.components.TFDAppBar

@Composable
fun HomeScreen(
    onActionButtonClicked: () -> Unit,
    onTripButtonClicked: () -> Unit,
    onFreightButtonClicked: () -> Unit
) {

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Tools For Driver",
                actionIcon = Icons.AutoMirrored.Filled.Logout,
                actionIconDescription = stringResource(id = R.string.log_out),
                onActionIconClicked = { onActionButtonClicked() }
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(id = R.string.trips)
                ) {
                    onTripButtonClicked()
                }
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = stringResource(id = R.string.freights)
                ) {
                    onFreightButtonClicked()
                }
            }
        }
    }
}
