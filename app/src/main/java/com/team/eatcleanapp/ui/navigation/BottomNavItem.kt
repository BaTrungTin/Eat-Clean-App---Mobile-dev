package com.team.eatcleanapp.ui.navigation

import com.team.eatcleanapp.R

enum class BottomNavItem (
    val route: String,
    val iconRes: Int,
    val contentDescription: String
) {
    HOME(
        "home",
        R.drawable.home,
        "Go to home"
    ),

    MENU(
        "menu",
        R.drawable.menu,
        "Go to search"
    ),

    FAVORITE(
        "favorite",
        R.drawable.heart,
        "Go to favorite"
    ),

    DAILY_MENU(
        "dailymenu",
        R.drawable.dailymenu,
        "Go to daily menu"
    )
}