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
import com.example.toolsfordriver.ui.screens.tripsreport.TripsReportScreen

@Composable
fun TFDNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TFDScreens.SplashScreen.name
    ) {
        composable(TFDScreens.SplashScreen.name) {
            SplashScreen(
                onNavigateToAuthScreen = { navController.navigate(TFDScreens.AuthScreen.name) },
                onNavigateToHomeScreen = { navController.navigate(TFDScreens.HomeScreen.name) }
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
                onNavigateToAuthScreen = { navController.navigate(TFDScreens.AuthScreen.name) }
            )
        }

        composable(TFDScreens.MyProfileScreen.name) {
            MyProfileScreen(
                onNavigateToHomeScreen = { navController.navigate(TFDScreens.HomeScreen.name) },
                onNavigateToAuthScreen = { navController.navigate(TFDScreens.AuthScreen.name) }
            )
        }

        composable(TFDScreens.HomeScreen.name) {
            HomeScreen(
                onNavigateToMyProfileScreen = {
                    navController.navigate(TFDScreens.MyProfileScreen.name)
                },
                onNavigateToTripsScreen = {
                    navController.navigate(TFDScreens.TripsScreen.name)
                },
                onNavigateToFreightsScreen = {
                    navController.navigate(TFDScreens.FreightsScreen.name)
                }
            )
        }

        composable(TFDScreens.TripsScreen.name) {
            TripsScreen (
                onNavigationToHomeScreen = {
                    navController.navigate(TFDScreens.HomeScreen.name)
                },
                onNavigationToTripsReportScreen = {
                    navController.navigate("${TFDScreens.TripsReportScreen.name}/$it")
                }
            )
        }

        composable(TFDScreens.FreightsScreen.name) {
            FreightsScreen { navController.navigate(TFDScreens.HomeScreen.name) }
        }

        composable(
            route = "${TFDScreens.TripsReportScreen.name}/{period}",
            arguments = listOf(
                navArgument("period") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val period = it.arguments?.getString("period") ?: ""
            TripsReportScreen(
                period = period,
                onNavIconClicked = { navController.popBackStack() }
            )
        }
    }
}