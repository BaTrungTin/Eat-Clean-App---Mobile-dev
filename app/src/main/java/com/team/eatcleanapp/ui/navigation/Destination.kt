package com.team.eatcleanapp.ui.navigation

import androidx.annotation.DrawableRes
import com.team.eatcleanapp.R

enum class Destination(
    val route: String,
    @DrawableRes val iconRes: Int,
    val contentDescription: String
) {
    HOME("home", R.drawable.home, "Home"),
    MENU("menu", R.drawable.menu, "Menu"),
    FAVORITE("favorite", R.drawable.heart, "Favorite"),
    PROFILE("profile", R.drawable.boy, "Profile")
}
