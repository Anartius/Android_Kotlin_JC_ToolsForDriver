package com.example.toolsfordriver.screens.home

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toolsfordriver.components.AppButton
import com.example.toolsfordriver.components.TFDAppBar
import com.example.toolsfordriver.navigation.TFDScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TFDAppBar(
                title = "Tools For Driver",
                actionIcon = Icons.AutoMirrored.Filled.Logout,
                actionIconDescription = "Log Out",
                onActionIconClicked = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(TFDScreens.AuthScreen.name)
                    }
                }
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
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = "Trips"
                ) {
                    navController.navigate(TFDScreens.TripListScreen.name)
                }
                AppButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp, vertical = 8.dp),
                    buttonText = "Freights"
                ) {
                    navController.navigate(TFDScreens.FreightListScreen.name)
                }
            }
        }
    }
}
