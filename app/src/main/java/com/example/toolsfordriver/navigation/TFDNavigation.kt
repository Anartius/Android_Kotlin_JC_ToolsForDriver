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
import com.example.toolsfordriver.screens.list.ListScreen
import com.example.toolsfordriver.screens.list.ListScreenViewModel
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

        val listScreenName = TFDScreens.ListScreen.name
        composable(
            route = "$listScreenName/{listItemName}",
            arguments = listOf(
                navArgument("listItemName") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val listScreenViewModel = hiltViewModel<ListScreenViewModel>()
            navBackStackEntry.arguments?.getString("listItemName").let { itemName ->
                ListScreen(
                    navController = navController,
                    viewModel = listScreenViewModel,
                    itemName = itemName.toString()
                )
            }
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

    }
}