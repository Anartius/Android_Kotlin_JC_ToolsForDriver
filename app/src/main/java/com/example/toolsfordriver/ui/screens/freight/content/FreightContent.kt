package com.example.toolsfordriver.ui.screens.freight.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.screens.freight.FreightViewModel

@Composable
fun FreightContent(
    viewModel: FreightViewModel,
    onNavIconClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(id = R.string.freight),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = onNavIconClicked
            )
        },
        floatingActionButton = { FreightContentFAB(viewModel = viewModel) { onNavIconClicked() } },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValue ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                FreightContentLazyColumn(viewModel = viewModel)
            }
        }
    }
}