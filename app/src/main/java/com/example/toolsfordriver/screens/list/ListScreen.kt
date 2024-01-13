package com.example.toolsfordriver.screens.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListScreenViewModel,
    itemName: String
) {
    val itemsList = viewModel.tripList.collectAsState().value

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "$itemName List",
                navIcon = Icons.Default.KeyboardArrowLeft,
                navIconDescription = "Back",
                onNavIconClicked = {
                    navController.navigate(TFDScreens.HomeScreen.name)
                }
            )
        },
        floatingActionButton = {
            FABContent(fabDescription = "Add $itemName") {
                navController.navigate(TFDScreens.TripScreen.name + "/new")
            }
        }
    ) { paddingValue ->
        Surface (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
        ) {
            LazyColumn {
                items(items = itemsList) { item ->
                    when (itemName) {
                        "Trip" -> {
                            TripRow(
                                trip = item,
                                navController = navController,
                                viewModel
                            )
                        }
                        else -> Text(text = "No data available")
                    }
                }
            }
        }
    }

}

@Composable
fun FABContent(
    fabDescription: String,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = colorResource(id = R.color.light_blue),
        modifier = Modifier.size(55.dp),
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = fabDescription,
            tint = Color.White,
            modifier = Modifier.size(35.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripRow(
    trip: TripDBModel,
    navController: NavController,
    viewModel: ListScreenViewModel
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
//                    viewModel.addTrip(
//                        TripDBModel(
//                            userId = FirebaseAuth.getInstance().currentUser!!.uid,
//                            startTime = "2024-01-10-21",
//                            endTime = "2024-01-12-24"
//                        )
//                    )
                    viewModel.deleteTrip(trip)
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
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "${trip.startTime} -> ${trip.endTime}")
        }
    }
}
