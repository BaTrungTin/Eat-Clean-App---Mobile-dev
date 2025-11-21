package com.team.eatcleanapp.data.remote

data class MealDto(
    val id: String = "",
    val name: String = "",
    val calories: Double = 0.0,
    val image: String? = null,
    val ingredients: List<IngredientDto> = emptyList(),
    val instructions: List<String> = emptyList()
)

data class IngredientDto(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val caloriesPer100: Double = 0.0,
    val carbsPer100: Double = 0.0,
    val proteinPer100: Double = 0.0,
    val fatPer100: Double = 0.0
)
