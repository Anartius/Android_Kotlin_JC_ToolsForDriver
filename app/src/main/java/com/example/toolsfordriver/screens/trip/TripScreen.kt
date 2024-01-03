package com.example.toolsfordriver.screens.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.toolsfordriver.data.TripDBModel
import com.example.toolsfordriver.navigation.TFDScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TripScreen(
    navController: NavController,
    viewModel: TripScreenViewModel = hiltViewModel()
) {
    Surface (modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Trip Screen")
            Button(
                onClick = {
                    viewModel.addTrip(
                        TripDBModel(
                            userId = FirebaseAuth.getInstance().currentUser!!.uid,
                            startTime = "2024-01-01-21",
                            endTime = ""
                        )
                    )
                    navController.navigate(TFDScreens.HomeScreen.name)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 30.dp),
                shape = CircleShape
            ) {
                Text(text = "Add Trip")
            }
        }
    }
}