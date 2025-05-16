package com.example.toolsfordriver.ui.screens.trip.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.toolsfordriver.R
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.ui.common.ActionIcon
import com.example.toolsfordriver.ui.common.FABContent
import com.example.toolsfordriver.ui.common.SwipeableItemWithActions
import com.example.toolsfordriver.ui.common.TFDAppBar
import com.example.toolsfordriver.ui.common.dialogs.ActionConfirmDialog
import com.example.toolsfordriver.ui.screens.trip.TripViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TripListContent(onNavIconClicked: () -> Unit) {
    val viewModel: TripViewModel = hiltViewModel()

    val tripList = viewModel.trips.collectAsStateWithLifecycle(emptyList()).value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val swipedItemId = uiState.swipedItemId
    val showDeleteConfDialog =uiState.showDeleteItemConfDialog

    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = true) { onNavIconClicked() }

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
        LaunchedEffect(swipedItemId) { }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(horizontal = 8.dp)
        ) {
            items(items = tripList.asReversed()) { item ->
                var isRevealed = swipedItemId == item.id

                SwipeableItemWithActions(
                    isRevealed = isRevealed,
                    actions = {
                        ActionIcon(
                            icon = Icons.Outlined.Delete,
                            iconDescription = stringResource(R.string.delete)
                                    + stringResource(R.string.trip),
                            modifier = Modifier.fillMaxHeight().padding(start = 30.dp),
                            tint = Color.Red
                        ) {
                            viewModel.addTripToDelete(trip = item)
                            viewModel.showDeleteItemConfDialog(true)
                        }
                    },
                    onExpanded = { viewModel.updateSwipedItemId(item.id) },
                    onCollapsed = { viewModel.updateSwipedItemId("") },
                    onSwipeDetected = { viewModel.updateSwipedItemId("") }
                ) {
                    TripRow(
                        trip = item,
                        onClick = {
                            viewModel.updateCurrentTrip(item)
                            viewModel.setCurrentTripAsNew(false)
                            viewModel.showTripContent(true)
                        }
                    )
                }

                if (showDeleteConfDialog) {
                    ActionConfirmDialog(
                        title = stringResource(R.string.trip_delete),
                        message = stringResource(R.string.ask_to_trip_delete),
                        onConfirm = {
                            viewModel.deleteTrip()
                            viewModel.updateSwipedItemId("")
                        },
                        onDismiss = {
                            viewModel.showDeleteItemConfDialog(false)
                            viewModel.updateSwipedItemId("")
                        }
                    )
                }
            }
        }
    }
}


//    LaunchedEffect(viewModel.snackbarMessages) {
//        viewModel.snackbarMessages.collect { snackbarMessage ->
//            val job = launch {
//                snackbarHostState.showSnackbar(
//                    message = snackbarMessage.asString(context),
//                    duration = SnackbarDuration.Indefinite
//                )
//            }
//            delay(5000)
//            job.cancel()
//        }
//    }
