package com.example.toolsfordriver.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.toolsfordriver.ui.screens.SplashScreen
import com.example.toolsfordriver.ui.screens.auth.AuthScreen
import com.example.toolsfordriver.ui.screens.freight.FreightsScreen
import com.example.toolsfordriver.ui.screens.home.HomeScreen
import com.example.toolsfordriver.ui.screens.myprofile.MyProfileScreen
import com.example.toolsfordriver.ui.screens.passwordreset.PasswordResetScreen
import com.example.toolsfordriver.ui.screens.trip.TripsScreen

@Composable
fun TFDNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TFDScreens.SplashScreen.name
    ) {
        composable(TFDScreens.SplashScreen.name) {
            SplashScreen(
                toAuthScreen = { navController.navigate(TFDScreens.AuthScreen.name) },
                toHomeScreen = { navController.navigate(TFDScreens.HomeScreen.name) }
            )
        }

        composable(TFDScreens.AuthScreen.name) {
            AuthScreen(onAuthSuccess = { navController.navigate(TFDScreens.HomeScreen.name) })
        }

        composable(
            route = "${TFDScreens.PasswordResetScreen.name}/{oobCode}",
            arguments = listOf(
                navArgument("oobCode") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://tfdapp-b8464.firebaseapp.com/__/auth" +
                                    "/action?mode=resetPassword&oobCode={oobCode}"
                    action = Intent.ACTION_VIEW
                }

            )
        ) { backStackEntry ->
            val oobCode = backStackEntry.arguments?.getString("oobCode") ?: ""
            PasswordResetScreen(
                oobCode = oobCode,
                onNavIconClicked = { navController.navigate(TFDScreens.AuthScreen.name) }
            )
        }

        composable(TFDScreens.MyProfileScreen.name) {
            MyProfileScreen(
                onNavIconClicked = { navController.navigate(TFDScreens.HomeScreen.name) },
                onSignOutIconClicked = { navController.navigate(TFDScreens.AuthScreen.name) }
            )
        }

        composable(TFDScreens.HomeScreen.name) {
            HomeScreen(
                onAccountIconClicked = { navController.navigate(TFDScreens.MyProfileScreen.name) },
                onTripButtonClicked = { navController.navigate(TFDScreens.TripsScreen.name) },
                onFreightButtonClicked = { navController.navigate(TFDScreens.FreightsScreen.name) }
            )
        }

        composable(TFDScreens.TripsScreen.name) {
            TripsScreen { navController.navigate(TFDScreens.HomeScreen.name) }
        }

        composable(TFDScreens.FreightsScreen.name) {
            FreightsScreen { navController.navigate(TFDScreens.HomeScreen.name) }
        }
    }
}