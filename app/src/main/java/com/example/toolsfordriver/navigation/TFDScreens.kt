package com.example.toolsfordriver.navigation

enum class TFDScreens {
    SplashScreen,
    AuthScreen,
    HomeScreen,
    TripsScreen,
    FreightsScreen;

    companion object {
        fun fromRoute(route: String?): TFDScreens =
            when(route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                AuthScreen.name -> AuthScreen
                TripsScreen.name -> TripsScreen
                FreightsScreen.name -> FreightsScreen
                null -> HomeScreen
                else ->throw (IllegalArgumentException("Route $route isn't recognised."))
            }

    }
}