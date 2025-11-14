package com.team.eatcleanapp.domain.model

data class Meal(
    val id: String?,
    val name: String,
    val calories: Double,
    val image: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val category: MealCategory,
    val isFavorite: Boolean
)

enum class MealCategory {
    BREAKFAST,
    LUNCH,
    DINNER
}