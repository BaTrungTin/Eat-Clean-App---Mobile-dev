package com.team.eatcleanapp.domain.model

data class Meal(
    val id: String?,
    val name: String,
    val calories: Double,
    val image: String? = null,
    val ingredients: String,
    val instructions: String
)
