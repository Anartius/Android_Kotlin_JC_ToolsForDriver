package com.example.toolsfordriver.screens.triplist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.DeleteItemPopup
import com.example.toolsfordriver.components.FABContent
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.example.toolsfordriver.utils.dateAsString
import com.example.toolsfordriver.utils.timeAsString

@Composable
fun TripListScreen(
    navController: NavController,
    viewModel: TripListScreenViewModel
) {
    val tripList = viewModel.tripList.collectAsState().value.sortedBy { it.startTime }
    val showDeletePopup = remember { mutableStateOf(false) }
    val tripToDelete = remember { mutableStateOf<TripDBModel?>(null) }

    BackHandler(enabled = true) {
        navController.navigate(TFDScreens.HomeScreen.name)
    }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Trips",
                navIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                navIconDescription = "Back",
                onNavIconClicked = {
                    navController.navigate(TFDScreens.HomeScreen.name)
                }
            )
        },
        floatingActionButton = {
            FABContent(fabDescription = "add trip") {
                navController.navigate(TFDScreens.TripScreen.name + "/new")
            }
        }
    ) { paddingValue ->
        Surface (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
        ) {
            LazyColumn {
                items(items = tripList) { item ->
                    TripRow(
                        trip = item,
                        tripToDelete = tripToDelete,
                        navController = navController,
                        showDeletePopup = showDeletePopup
                    )
                }
            }
        }

        if (showDeletePopup.value) {
            DeleteItemPopup(
                showDeletePopup = showDeletePopup,
                itemName = "trip"
            ) {
                tripToDelete.value?.let {
                    viewModel.deleteTrip(it)
                }
                tripToDelete.value = null
                showDeletePopup.value = false
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripRow(
    trip: TripDBModel,
    tripToDelete: MutableState<TripDBModel?>,
    navController: NavController,
    showDeletePopup: MutableState<Boolean>
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 6.dp)
            .combinedClickable(
                enabled = true,
                onClick = {
                    navController.navigate(TFDScreens.TripScreen.name + "/${trip.id}")
                },
                onLongClick = {
                    tripToDelete.value = trip
                    showDeletePopup.value = true
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
                    color = Color.Gray)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow Forward",
                tint = Color.Gray,
            )
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateAsString(trip.endTime))
                Text(
                    text = timeAsString(trip.endTime),
                    color = Color.Gray)
            }
        }
    }
}
