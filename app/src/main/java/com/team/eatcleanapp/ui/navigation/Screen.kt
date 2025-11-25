package com.team.eatcleanapp.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object HealthProfile : Screen("health_profile")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
    object MealDetail : Screen("meal_detail/{mealId}") {
        fun createRoute(mealId: String) = "meal_detail/$mealId"
    }

    object Favorite : Screen("favorite")
    object DailyMenu : Screen("dailymenu")
    object Menu : Screen("menu")
    object Settings : Screen("settings")
}
