package com.example.toolsfordriver.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toolsfordriver.ui.screens.FreightsScreen
import com.example.toolsfordriver.ui.screens.HomeScreen
import com.example.toolsfordriver.ui.screens.SplashScreen
import com.example.toolsfordriver.ui.screens.TripsScreen
import com.example.toolsfordriver.ui.screens.auth.AuthScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TFDNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = TFDScreens.SplashScreen.name
    ) {
        composable(TFDScreens.SplashScreen.name) {
            SplashScreen(
                onStartNavigation = {
                    if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                        navController.navigate(TFDScreens.AuthScreen.name)
                    } else {
                        navController.navigate(TFDScreens.HomeScreen.name)
                    }
                }
            )
        }
        composable(TFDScreens.AuthScreen.name) {
            AuthScreen(onAuthSuccess = { navController.navigate(TFDScreens.HomeScreen.name) })
        }
        composable(TFDScreens.HomeScreen.name) {
            HomeScreen(
                onActionButtonClicked = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(TFDScreens.AuthScreen.name)
                    }
                },
                onTripButtonClicked = {
                    navController.navigate(TFDScreens.TripsScreen.name)
                },
                onFreightButtonClicked = {
                    navController.navigate(TFDScreens.FreightsScreen.name)
                }
            )
        }

        composable(TFDScreens.TripsScreen.name) {
            TripsScreen {
                navController.navigate(TFDScreens.HomeScreen.name)
            }
        }

        composable(TFDScreens.FreightsScreen.name) {
            FreightsScreen {
                navController.navigate(TFDScreens.HomeScreen.name)
            }
        }
    }
}