package com.example.toolsfordriver.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.toolsfordriver.screens.home.HomeScreen
import com.example.toolsfordriver.screens.SplashScreen
import com.example.toolsfordriver.screens.auth.AuthScreen
import com.example.toolsfordriver.screens.freight.FreightScreen
import com.example.toolsfordriver.screens.freight.FreightScreenViewModel
import com.example.toolsfordriver.screens.freightlist.FreightListScreen
import com.example.toolsfordriver.screens.freightlist.FreightListScreenViewModel
import com.example.toolsfordriver.screens.triplist.TripListScreen
import com.example.toolsfordriver.screens.triplist.TripListScreenViewModel
import com.example.toolsfordriver.screens.trip.TripScreen
import com.example.toolsfordriver.screens.trip.TripScreenViewModel

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

        composable(TFDScreens.TripListScreen.name) {
            val tripListScreenViewModel = hiltViewModel<TripListScreenViewModel>()
            TripListScreen(
                navController = navController,
                viewModel = tripListScreenViewModel
            )
        }

        val tripScreenName = TFDScreens.TripScreen.name
        composable(
            route = "$tripScreenName/{tripId}",
            arguments = listOf(
                navArgument("tripId") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val tripScreenViewModel = hiltViewModel<TripScreenViewModel>()
            navBackStackEntry.arguments?.getString("tripId").let { tripId ->
                TripScreen(
                    navController = navController,
                    viewModel = tripScreenViewModel,
                    tripId = tripId.toString()
                )
            }
        }

        composable(TFDScreens.FreightListScreen.name) {
            val freightListScreenViewModel = hiltViewModel<FreightListScreenViewModel>()
            FreightListScreen(
                navController = navController,
                viewModel = freightListScreenViewModel
            )
        }

        val freightScreenName = TFDScreens.FreightScreen.name
        composable(
            route = "$freightScreenName/{freightId}",
            arguments = listOf(
                navArgument("freightId") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val freightScreenViewModel = hiltViewModel<FreightScreenViewModel>()
            navBackStackEntry.arguments?.getString("freightId").let { freightId ->
                FreightScreen(
                    navController = navController,
                    viewModel = freightScreenViewModel,
                    freightId = freightId.toString()
                )
            }
        }
    }
}