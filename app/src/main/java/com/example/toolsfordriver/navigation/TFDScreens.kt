package com.example.toolsfordriver.navigation

enum class TFDScreens {
    AuthScreen,
    HomeScreen,
    FreightsScreen,
    MyProfileScreen,
    PasswordResetScreen,
    SplashScreen,
    TripsScreen,
    TripsReportScreen,
    TripsReportMenuScreen;

    companion object {
        fun fromRoute(route: String?): TFDScreens =
            when(route?.substringBefore("/")) {
                AuthScreen.name -> AuthScreen
                FreightsScreen.name -> FreightsScreen
                MyProfileScreen.name -> MyProfileScreen
                PasswordResetScreen.name -> PasswordResetScreen
                SplashScreen.name -> SplashScreen
                TripsScreen.name -> TripsScreen
                TripsReportScreen.name -> TripsReportScreen
                TripsReportMenuScreen.name -> TripsReportMenuScreen
                null -> HomeScreen
                else ->throw (IllegalArgumentException("Route $route isn't recognised."))
            }

    }
}