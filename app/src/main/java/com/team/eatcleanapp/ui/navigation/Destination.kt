package com.team.eatcleanapp.ui.navigation

import com.team.eatcleanapp.R

enum class Destination (
    val route: String,
    val iconRes: Int,
    val contentDescription: String
) {
    COMMUNITY(
        "community",
        R.drawable.community,
        "Go to community"
    ),

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

    CALENDAR(
        "calendar",
        R.drawable.calendar,
        "Go to calendar"
    ),

    SEARCH(
        "search",
        R.drawable.search,
        "Go to search"
    )
}