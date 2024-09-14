package com.example.toolsfordriver.navigation

enum class TFDScreens {
    SplashScreen,
    AuthScreen,
    MyProfileScreen,
    HomeScreen,
    TripsScreen,
    FreightsScreen,
    CameraScreen;

    companion object {
        fun fromRoute(route: String?): TFDScreens =
            when(route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                AuthScreen.name -> AuthScreen
                MyProfileScreen.name -> MyProfileScreen
                TripsScreen.name -> TripsScreen
                FreightsScreen.name -> FreightsScreen
                null -> HomeScreen
                else ->throw (IllegalArgumentException("Route $route isn't recognised."))
            }

    }
}