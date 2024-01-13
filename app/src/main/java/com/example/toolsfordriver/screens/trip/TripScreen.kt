package com.example.toolsfordriver.screens.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.R
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.navigation.TFDScreens

@Composable
fun TripScreen(
    navController: NavController,
    viewModel: TripScreenViewModel,
    tripId: String
) {
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Trip",
                navIcon = Icons.Default.KeyboardArrowLeft,
                navIconDescription = "Back",
                onNavIconClicked = {
                    navController.navigate(TFDScreens.ListScreen.name + "/Trip")
                }
            )
        }
    ) { paddingValue ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                val tripList = if (tripId != "new") {
                    viewModel.tripList.collectAsState().value
                } else emptyList()

                if (tripList.isEmpty() && tripId != "new") {
                    CircularProgressIndicator()
                } else {
                    if (tripList.isNotEmpty()) {
                        startTime = tripList.first().startTime
                        endTime = tripList.first().endTime
                    }
                    TextRpw(description = "Start:", value = startTime)
                    TextRpw(description = "Finish:", value = endTime)
                    TextRpw(description = "Duration:", value = "5d. 15h.")
                    TextRpw(description = "Earnings:", value = "3450 zł.")

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            navController.navigate(TFDScreens.HomeScreen.name)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp, vertical = 20.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = colorResource(id = R.color.light_blue)
                        )
                    ) {
                        Text(text = "Edit Trip")
                    }
                }
            }
        }
    }
}

@Composable
fun TextRpw(description: String, value: String) {
    Row (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = description)
        Text(text = value)
    }
}