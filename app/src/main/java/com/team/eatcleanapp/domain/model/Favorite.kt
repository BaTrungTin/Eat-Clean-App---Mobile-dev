package com.team.eatcleanapp.domain.model

import java.util.Date

data class Favorite(
    val userId: String,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val image: String,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val isCustomized: Boolean = false
)
