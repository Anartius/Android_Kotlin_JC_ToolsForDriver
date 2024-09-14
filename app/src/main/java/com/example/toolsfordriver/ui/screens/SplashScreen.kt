package com.example.toolsfordriver.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.toolsfordriver.R
import com.example.toolsfordriver.ui.screens.auth.AuthScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    toAuthScreen: () -> Unit,
    toHomeScreen: () -> Unit
) {
    val viewModel: AuthScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        delay(700L)
        if (viewModel.isUserAuthenticated()) toHomeScreen() else toAuthScreen()
    }

    Surface(modifier = Modifier
        .padding(15.dp)
        .fillMaxSize()) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Tools For Driver",
                style = MaterialTheme.typography.titleLarge,
                color = colorResource(id = R.color.light_blue))
        }
    }
}