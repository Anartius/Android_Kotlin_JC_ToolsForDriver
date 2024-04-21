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
import com.example.toolsfordriver.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onStartNavigation: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(700L)
        onStartNavigation()
    }

    Surface(modifier = Modifier.padding(15.dp).fillMaxSize()) {
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