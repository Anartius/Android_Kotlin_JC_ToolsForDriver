package com.example.toolsfordriver.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toolsfordriver.screens.home.HomeScreen
import com.example.toolsfordriver.screens.SplashScreen
import com.example.toolsfordriver.screens.auth.AuthScreen
import com.example.toolsfordriver.screens.trip.TripScreen

@Composable
fun TFDNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = TFDScreens.SplashScreen.name
    ) {
        composable(TFDScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(TFDScreens.AuthScreen.name) {
            AuthScreen(navController = navController)
        }
        composable(TFDScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(TFDScreens.TripScreen.name) {
            TripScreen(navController = navController)
        }
    }
}