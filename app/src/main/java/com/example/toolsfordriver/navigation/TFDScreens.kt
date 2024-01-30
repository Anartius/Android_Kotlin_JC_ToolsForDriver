package com.example.toolsfordriver.navigation

enum class TFDScreens {
    SplashScreen,
    AuthScreen,
    HomeScreen,
    TripListScreen,
    TripScreen,
    FreightListScreen,
    FreightScreen;

    companion object {
        fun fromRoute(route: String?): TFDScreens =
            when(route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                AuthScreen.name -> AuthScreen
                TripListScreen.name -> TripListScreen
                TripScreen.name -> TripScreen
                FreightListScreen.name -> FreightListScreen
                FreightScreen.name -> FreightScreen
                null -> HomeScreen
                else ->throw (IllegalArgumentException("Route $route isn't recognised."))
            }

    }
}