package com.example.toolsfordriver.navigation

enum class TFDScreens {
    SplashScreen,
    AuthScreen,
    HomeScreen,
    ListScreen,
    UpdateWorkTimeScreen,
    UpdateTripScreen;

    companion object {
        fun fromRoute(route: String?): TFDScreens =
            when(route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                AuthScreen.name -> AuthScreen
                ListScreen.name -> ListScreen
                UpdateWorkTimeScreen.name -> UpdateWorkTimeScreen
                UpdateTripScreen.name -> UpdateTripScreen
                null -> HomeScreen
                else ->throw (IllegalArgumentException("Route $route isn't recognised."))
            }

    }
}