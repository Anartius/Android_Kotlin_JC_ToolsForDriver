package com.example.toolsfordriver.navigation

enum class TFDScreens {
    AuthScreen,
    HomeScreen,
    FreightsScreen,
    MyProfileScreen,
    PasswordResetScreen,
    SplashScreen,
    TripsScreen;

    companion object {
        fun fromRoute(route: String?): TFDScreens =
            when(route?.substringBefore("/")) {
                AuthScreen.name -> AuthScreen
                FreightsScreen.name -> FreightsScreen
                MyProfileScreen.name -> MyProfileScreen
                PasswordResetScreen.name -> PasswordResetScreen
                SplashScreen.name -> SplashScreen
                TripsScreen.name -> TripsScreen
                null -> HomeScreen
                else ->throw (IllegalArgumentException("Route $route isn't recognised."))
            }

    }
}