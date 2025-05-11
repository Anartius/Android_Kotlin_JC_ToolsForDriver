package com.example.toolsfordriver.ui.screens.trip.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.ui.common.DeleteItemPopup
import com.example.toolsfordriver.ui.common.FABContent
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.screens.trip.TripViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TripListContent(onNavIconClicked: () -> Unit) {
    val viewModel: TripViewModel = hiltViewModel()
    val tripList = viewModel.trips.collectAsStateWithLifecycle(emptyList()).value
    val showDeletePopup = viewModel.uiState.collectAsStateWithLifecycle().value.showDeletePopup
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackHandler(enabled = true) { onNavIconClicked() }

    LaunchedEffect(viewModel.snackbarMessages) {
        viewModel.snackbarMessages.collect { snackbarMessage ->
            val job = launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage.asString(context),
                    duration = SnackbarDuration.Indefinite
                )
            }
            delay(5000)
            job.cancel()
        }
    }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = stringResource(id = R.string.trips),
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                onNavIconClicked = onNavIconClicked
            )
        },
        floatingActionButton = {
            FABContent(fabDescription = stringResource(id = R.string.add_trip)) {
                viewModel.updateCurrentTrip(
                    Trip(userId = FirebaseAuth.getInstance().currentUser!!.uid)
                )
                viewModel.setCurrentTripAsNew(true)
                viewModel.showTripContent(true)
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        }
    ) { paddingValue ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(horizontal = 8.dp)
        ) {
            LazyColumn {
                items(items = tripList.asReversed()) { item ->
                    TripRow(
                        trip = item,
                        viewModel = viewModel
                    )
                }
            }
        }

        if (showDeletePopup) {
            DeleteItemPopup(
                itemName = stringResource(id = R.string.trip),
                onDismiss = { viewModel.showDeletePopup(false) }
            ) { viewModel.deleteTrip() }
        }
    }
}
