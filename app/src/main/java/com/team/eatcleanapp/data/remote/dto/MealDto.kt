package com.team.eatcleanapp.data.remote.dto

data class MealDto(
    val id: String = "",
    val name: String = "",
    val calories: Double = 0.0,
    val image: String? = null,
    val ingredients: List<IngredientDto> = emptyList(),
    val instructions: List<String> = emptyList()
)
