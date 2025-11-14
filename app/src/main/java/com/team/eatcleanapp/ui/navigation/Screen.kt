package com.team.eatcleanapp.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Register : Screen("register")
    object Login : Screen("login")

    data class HealthCalculator(val mode: Mode, val focusField: String?) :
        Screen("healthProfile/{mode}/{focusField}") {
        enum class Mode { BMI, TDEE }

        fun createRoute(mode: Mode, focusField: String? = null) : String {
            return "healthProfile/${mode.name}/${focusField ?: "none"}"
        }
    }

    object Goal : Screen("goal")
    object Home : Screen("home")
    object Menu : Screen("menu")

    data class MealDetail(val id: Int) :
        Screen("meal_detail/{id}") {
        fun createRoute(id: Int): String = "meal_detail/$id"
    }

    object Favorites : Screen("favorites")
    object DailyMenu : Screen("daily_menu")
    object Profile : Screen("profile")
}