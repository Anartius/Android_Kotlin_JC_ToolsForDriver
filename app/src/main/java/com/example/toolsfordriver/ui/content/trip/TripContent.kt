package com.example.toolsfordriver.ui.content.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.TFDViewModel
import com.example.toolsfordriver.ui.components.AppButton
import com.example.toolsfordriver.ui.components.DateTimeDialog
import com.example.toolsfordriver.ui.components.TFDAppBar
import com.example.toolsfordriver.ui.components.TextRow
import com.example.toolsfordriver.utils.calcEarnings
import com.example.toolsfordriver.utils.calcPeriod
import com.example.toolsfordriver.utils.dateAsString
import com.example.toolsfordriver.utils.dateAsStringIso
import com.example.toolsfordriver.utils.formatPeriod
import com.example.toolsfordriver.utils.timeAsString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun TripContent(
    viewModel: TFDViewModel,
    onNavIconClicked: () -> Unit
) {
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
                    .padding(horizontal = 5.dp, vertical = 25.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                TripContentColumn(viewModel = viewModel)
            }
        }
    }
}