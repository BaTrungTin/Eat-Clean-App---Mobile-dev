package com.team.eatcleanapp.ui.navigation

import com.team.eatcleanapp.R

enum class BottomNavItem (
    val route: String,
    val iconRes: Int,
    val contentDescription: String
) {
    FAVORITE(
        "favorite",
        R.drawable.heart,
        "Go to favorite"
    ),

    HOME(
        "home",
        R.drawable.home,
        "Go to home"
    ),

    DAILYMENU(
        "dailymenu",
        R.drawable.dailymenu,
        "Go to daily menu"
    ),

    MENU(
        "menu",
        R.drawable.menu,
        "Go to search"
    )
}