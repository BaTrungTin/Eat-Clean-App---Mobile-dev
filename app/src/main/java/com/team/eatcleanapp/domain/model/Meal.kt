package com.team.eatcleanapp.domain.model

data class Meal(
    val id: String,
    val name: String,
    val calories: Double,
    val image: String? = null,
    val ingredients: List<Ingredient>,
    val instructions: List<String>
)

data class Ingredient(
    val name: String,
    val amount: String
)